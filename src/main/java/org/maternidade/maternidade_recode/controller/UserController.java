package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // Alterado de @Controller para @RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint para listar usuários (agora retorna JSON)
    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // Endpoint para criar novo usuário (agora retorna JSON)
    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // Endpoint para editar usuário
    @GetMapping("/edit/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // Endpoint para deletar usuário
    @GetMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    // Novo endpoint para buscar a foto de perfil do usuário
    @GetMapping("/user-photo/{userId}")
    public ResponseEntity<Map<String, String>> getUserPhoto(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user != null && user.getFotoPerfil() != null) {
            Map<String, String> response = new HashMap<>();
            response.put("fotoPerfil", "http://localhost:8080/uploads/" + user.getFotoPerfil()); // URL completa
            response.put("nomeCompleto", user.getNomeCompleto());
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("fotoPerfil", "http://localhost:8080/uploads/default.jpg");
            return ResponseEntity.ok(response);
        }
    }
}
