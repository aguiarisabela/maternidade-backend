package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.LoginRequest;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        String identifier = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<User> userOptional = userService.findByUsername(identifier);
        if (!userOptional.isPresent()) {
            userOptional = userService.findByEmail(identifier);
        }

        if (userOptional.isPresent() && userOptional.get().getSenha().equals(password)) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login bem-sucedido");
        response.put("userId", userOptional.get().getId());
        response.put("identifier", userOptional.get().getUsername());
        return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Usuário ou senha inválidos!"));
        }
    }

    @GetMapping("/user-photo/{userId}")
    public ResponseEntity<Map<String, String>> getUserPhoto(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user != null && user.getFotoPerfil() != null) {
            Map<String, String> response = new HashMap<>();
            response.put("fotoPerfil", user.getFotoPerfil()); // Caminho da imagem no servidor
            response.put("nomeCompleto", user.getNomeCompleto()); // Opcional
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("fotoPerfil", "/default.jpg");
            return ResponseEntity.ok(response);
        }
    }
}