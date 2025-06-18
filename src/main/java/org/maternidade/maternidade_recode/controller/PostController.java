package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.FileUploadService;
import org.maternidade.maternidade_recode.service.PostService;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final FileUploadService fileUploadService;

    public PostController(PostService postService, UserService userService, FileUploadService fileUploadService) {
        this.postService = postService;
        this.userService = userService;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<?> createPost(
            @RequestPart("post") String postJson,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {
        try {
            System.out.println("JSON recebido: " + postJson); // Log do JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Post post = objectMapper.readValue(postJson, Post.class);
            Long autorId = post.getAutor().getId();
            User autor = userService.findById(autorId);
            if (autor == null) {
                return ResponseEntity.badRequest().body("Autor não encontrado para o ID: " + autorId);
            }
            post.setAutor(autor);
            post.setDataCriacao(LocalDateTime.now());
            post.setLikes(0);
            post.setComments(new java.util.ArrayList<>());

            if (imagem != null && !imagem.isEmpty()) {
                if (imagem.getSize() > 10 * 1024 * 1024) { // Limite de 10MB
                    return ResponseEntity.badRequest().body("Arquivo muito grande. Máximo 10MB.");
                }
                String imagePath = fileUploadService.saveFile(imagem);
                post.setImagem(imagePath);
            }

            Post savedPost = postService.save(post);
            System.out.println("Post salvo com ID: " + savedPost.getId()); // Log de sucesso
            return ResponseEntity.ok(savedPost);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erro ao processar arquivo: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao criar post: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestPart("post") String postJson,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {
        try {
            System.out.println("JSON recebido para edição: " + postJson); // Log do JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Post post = objectMapper.readValue(postJson, Post.class);
            Post existingPost = postService.findById(id);
            if (existingPost == null) {
                return ResponseEntity.badRequest().body("Post não encontrado.");
            }
            if (post.getAutor() == null || post.getAutor().getId() == null) {
                post.setAutor(existingPost.getAutor());
            } else if (!existingPost.getAutor().getId().equals(post.getAutor().getId())) {
                return ResponseEntity.badRequest().body("Você só pode editar seus próprios posts.");
            }
            post.setId(id);
            post.setDataCriacao(existingPost.getDataCriacao());
            post.setLikes(existingPost.getLikes());
            post.setComments(existingPost.getComments());

            if (imagem != null && !imagem.isEmpty()) {
                if (imagem.getSize() > 10 * 1024 * 1024) { // Limite de 10MB
                    return ResponseEntity.badRequest().body("Arquivo muito grande. Máximo 10MB.");
                }
                String imagePath;
                try {
                    imagePath = fileUploadService.saveFile(imagem);
                } catch (IOException e) {
                    return ResponseEntity.badRequest().body("Erro ao salvar imagem: " + e.getMessage());
                }
                post.setImagem(imagePath);
            } else {
                post.setImagem(existingPost.getImagem());
            }

            Post updatedPost = postService.save(post);
            System.out.println("Post atualizado com ID: " + updatedPost.getId()); // Log de sucesso
            return ResponseEntity.ok(updatedPost);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Erro ao processar arquivo: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao editar post: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @RequestParam Long userId) {
        try {
            Post post = postService.findById(id);
            if (post == null) {
                return ResponseEntity.badRequest().body("Post não encontrado.");
            }
            if (post.getAutor() == null || !post.getAutor().getId().equals(userId)) {
                return ResponseEntity.badRequest().body("Você só pode excluir seus próprios posts.");
            }
            postService.deleteById(id);
            return ResponseEntity.ok("Post excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir post: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody String comment) {
        try {
            Post post = postService.findById(id);
            if (post != null) {
                post.getComments().add(comment);
                postService.save(post);
                return ResponseEntity.ok(post);
            }
            return ResponseEntity.badRequest().body("Post não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao adicionar comentário: " + e.getMessage());
        }
    }
}