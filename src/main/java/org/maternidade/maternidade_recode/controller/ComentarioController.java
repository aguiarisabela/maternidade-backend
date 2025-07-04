package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.Comentario;
import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.repository.ComentarioRepository;
import org.maternidade.maternidade_recode.repository.PostRepository;
import org.maternidade.maternidade_recode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> criarComentario(@RequestBody Comentario comentario) {
        if (comentario.getTexto() == null || comentario.getTexto().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Comentário vazio.");
        }
        if (comentario.getAutor() == null || comentario.getAutor().getId() == null ||
            comentario.getPost() == null || comentario.getPost().getId() == null) {
            return ResponseEntity.badRequest().body("Usuário ou post inválido.");
        }
        // Busca entidades reais do banco
        User autor = userRepository.findById(comentario.getAutor().getId()).orElse(null);
        Post post = postRepository.findById(comentario.getPost().getId()).orElse(null);
        if (autor == null || post == null) {
            return ResponseEntity.badRequest().body("Usuário ou post não encontrado.");
        }
        comentario.setAutor(autor);
        comentario.setPost(post);
        comentario.setDataRegistro(java.time.LocalDateTime.now());
        comentarioRepository.save(comentario);
        return ResponseEntity.ok(comentario);
    }
    
}