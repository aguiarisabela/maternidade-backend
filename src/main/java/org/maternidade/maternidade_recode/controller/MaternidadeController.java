package org.maternidade.maternidade_recode.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.UserService;
import java.nio.file.Path;

@RestController
@RequestMapping("/api")
public class MaternidadeController {

    @GetMapping("/")
    public String exibirIndex() {
        return "index";
    }

    @GetMapping("/comunidade")
    public String exibirComunidade() {
        return "comunidade";
    }

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<String> register(
            @RequestPart("user") String userJson,
            @RequestPart(value = "fotoPerfil", required = false) MultipartFile fotoPerfil
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userJson, User.class);

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String contentType = fotoPerfil.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Apenas imagens são permitidas!");
            }
            

            String nomeArquivo = UUID.randomUUID() + "_" + fotoPerfil.getOriginalFilename();
            String caminhoFoto = "/uploads/" + nomeArquivo;
            user.setFotoPerfil(caminhoFoto);

            // Garante que a pasta existe
            Path destino = Paths.get("uploads/" + nomeArquivo);
            Files.createDirectories(destino.getParent());
            Files.write(destino, fotoPerfil.getBytes());
        }

        userService.save(user);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @GetMapping("/api/user/profile")
    public ResponseEntity<User> getUserProfile(@RequestParam String identifier) {
        Optional<User> userOptional = userService.findByUsername(identifier);
        if (!userOptional.isPresent()) {
            userOptional = userService.findByEmail(identifier);
        }
        return userOptional.map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
