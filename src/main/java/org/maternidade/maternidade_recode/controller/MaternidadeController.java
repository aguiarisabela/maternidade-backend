package org.maternidade.maternidade_recode.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.UserService;

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

    // ...existing code...
@Autowired
private UserService userService;
// ...existing code...

    // ... (outros métodos)

    @PostMapping("/login")
public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
    String identifier = loginRequest.getUsername(); // Pode ser username ou e-mail
    String password = loginRequest.getPassword();

    Optional<User> userOptional = userService.findByUsername(identifier); // Tenta por username
    if (!userOptional.isPresent()) {
        userOptional = userService.findByEmail(identifier); // Tenta por e-mail
    }
    if (userOptional.isPresent() && userOptional.get().getSenha().equals(password)) {
        return ResponseEntity.ok("Login bem-sucedido! Token: dummy-token-123");
    } else {
        return ResponseEntity.badRequest().body("Usuário ou senha inválidos!");
    }
}

    @PostMapping("/register")
public ResponseEntity<String> register(@RequestBody User user) {
    try {
        userService.save(user);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("Erro ao cadastrar usuário: " + e.getMessage());
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