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

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.save(user);
            return ResponseEntity.ok("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar usuário: " + e.getMessage());
        }
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