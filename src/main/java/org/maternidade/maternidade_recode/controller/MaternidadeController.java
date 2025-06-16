package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.FileUploadService;
import org.maternidade.maternidade_recode.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MaternidadeController {

    private static final Logger logger = LoggerFactory.getLogger(MaternidadeController.class);
    private final UserService userService;
    private final FileUploadService fileUploadService;

    public MaternidadeController(UserService userService, FileUploadService fileUploadService) {
        this.userService = userService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/")
    public String exibirIndex() {
        return "index";
    }

    @GetMapping("/comunidade")
    public String exibirComunidade() {
        return "comunidade";
    }

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<String> register(
            @RequestPart("user") User user,
            @RequestPart(value = "fotoPerfil", required = false) MultipartFile fotoPerfil) {
        try {
            if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
                String fotoPath = fileUploadService.saveFile(fotoPerfil);
                user.setFotoPerfil(fotoPath);
            }
            userService.save(user);
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro ao cadastrar usuário", e);
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String identifier = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<User> userOptional = userService.findByUsername(identifier);
        if (!userOptional.isPresent()) {
            userOptional = userService.findByEmail(identifier);
        }
        if (userOptional.isPresent() && userOptional.get().getSenha().equals(password)) {
            return ResponseEntity.ok("Bearer " + userOptional.get().getId());
        } else {
            return ResponseEntity.badRequest().body("Usuário ou senha inválidos!");
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}