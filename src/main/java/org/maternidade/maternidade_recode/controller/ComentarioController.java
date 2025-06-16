package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.Comentario;
import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.ComentarioService;
import org.maternidade.maternidade_recode.service.PostService;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/post/{postId}")
    public List<Comentario> getComentariosByPost(@PathVariable Long postId) {
        return comentarioService.findByPostId(postId);
    }

    @PostMapping
    public Comentario createComentario(@RequestBody Comentario comentario, @RequestHeader("Authorization") String token) {
        try {
            Long userId = Long.parseLong(token.replace("Bearer ", ""));
            User autor = userService.findById(userId);
            comentario.setAutor(autor);
            comentario.setDataRegistro(LocalDateTime.now());
            if (comentario.getPost() != null && comentario.getPost().getId() != null) {
                Post post = postService.findById(comentario.getPost().getId());
                comentario.setPost(post);
            }
            return comentarioService.save(comentario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar comentário: " + e.getMessage());
        }
    }
}