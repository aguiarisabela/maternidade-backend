package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.FileUploadService;
import org.maternidade.maternidade_recode.service.PostService;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    // Lista todos os posts
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    // Cria um novo post
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        // Temporário: usar usuário com ID 1 (depois conectaremos com login)
        User autor = userService.findById();
        post.setAutor(autor);
        post.setDataCriacao(LocalDateTime.now());
        return postService.save(post);
    }

    private final FileUploadService fileUploadService;

    public PostController(PostService postService, UserService userService, FileUploadService fileUploadService) {
        this.postService = postService;
        this.userService = userService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createPost(
            @RequestPart("post") Post post,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem,
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = Long.parseLong(token.replace("Bearer ", ""));
            User autor = userService.findById(userId);
            post.setAutor(autor);
            post.setDataCriacao(LocalDateTime.now());
            post.setLikes(0); // Initialize likes
            post.setComments(new ArrayList<>()); // Initialize comments

            if (imagem != null && !imagem.isEmpty()) {
                String imagePath = fileUploadService.saveFile(imagem);
                post.setImagem(imagePath);
            }

            Post savedPost = postService.save(post);
            return ResponseEntity.ok(savedPost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar post: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable Long id) {
        try {
            Post post = postService.findById(id);
            post.setLikes(post.getLikes() + 1);
            postService.save(post);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao curtir post: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody String comment) {
        try {
            Post post = postService.findById(id);
            post.getComments().add(comment);
            postService.save(post);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao adicionar comentário: " + e.getMessage());
        }
    }
    
}