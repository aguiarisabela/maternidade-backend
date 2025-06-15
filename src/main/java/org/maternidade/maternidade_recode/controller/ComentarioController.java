package org.maternidade.maternidade_recode.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.maternidade.maternidade_recode.model.Comentario;
import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.ComentarioService;
import org.maternidade.maternidade_recode.service.PostService;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final PostService postService;
    private final UserService userService;

    public ComentarioController(ComentarioService comentarioService, PostService postService, UserService userService) {
        this.comentarioService = comentarioService;
        this.postService = postService;
        this.userService = userService;
    }

    // Lista comentários de um post
    @GetMapping("/post/{postId}")
    public List<Comentario> getComentariosByPost(@PathVariable Long postId) {
        return comentarioService.findByPostId(postId);
    }

    // Cria um novo comentário
    @PostMapping
    public Comentario createComentario(@RequestBody Comentario comentario, @RequestHeader(value = "Authorization", required = false) String token) {
        // Simula usuário logado (futuramente usar JWT)
        String username = "user1"; // Substituir por extração do token
        User autor = userService.findByUsername(username).orElseThrow();
        comentario.setAutor(autor);
        comentario.setDataRegistro(LocalDateTime.now());
        if (comentario.getPost() != null && comentario.getPost().getId() != null) {
            Post post = postService.findById(comentario.getPost().getId());
            comentario.setPost(post);
        }
        return comentarioService.save(comentario);
    }
}