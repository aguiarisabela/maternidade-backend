package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.PostService;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    // Lista todos os posts
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    // Cria um novo post
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        // Temporário: usar usuário com ID 1 (depois conectaremos com login)
        User autor = userService.findById(1L);
        post.setAutor(autor);
        post.setDataCriacao(LocalDateTime.now());
        return postService.save(post);
    }
}