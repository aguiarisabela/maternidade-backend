package org.maternidade.maternidade_recode.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.maternidade.maternidade_recode.model.Comentario;
import org.maternidade.maternidade_recode.model.Like;
import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.repository.ComentarioRepository;
import org.maternidade.maternidade_recode.repository.LikeRepository;
import org.maternidade.maternidade_recode.repository.PostRepository;
import org.maternidade.maternidade_recode.repository.UserRepository;
import org.maternidade.maternidade_recode.service.FileUploadService;
import org.maternidade.maternidade_recode.service.PostService;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<Comentario>> getComentarios(


            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("dataRegistro").descending());
        Page<Comentario> comentariosPage = comentarioRepository.findByPostId(id, pageable);

        return ResponseEntity.ok(comentariosPage.getContent());
    }

    @Autowired
    private PostRepository postRepository;
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        try {
            List<Post> posts = postRepository.findAllWithAutor();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<?> createPost(
            @RequestPart("post") String postJson,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {
        try {
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
            post.setComentarios(new java.util.ArrayList<>());

            if (imagem != null && !imagem.isEmpty()) {
                if (imagem.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.badRequest().body("Arquivo muito grande. Máximo 10MB.");
                }
                String imagePath = fileUploadService.saveFile(imagem);
                post.setImagem(imagePath);
            }

            Post savedPost = postService.save(post);
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
            post.setComentarios(existingPost.getComentarios());

            if (imagem != null && !imagem.isEmpty()) {
                if (imagem.getSize() > 10 * 1024 * 1024) {
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

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            String texto = (String) payload.get("texto");
            Long autorId = payload.get("autorId") != null ? ((Number) payload.get("autorId")).longValue() : null;

            Post post = postService.findById(id);
            if (post == null) {
                return ResponseEntity.badRequest().body("Post não encontrado.");
            }
            User autor = userService.findById(autorId);
            if (autor == null) {
                return ResponseEntity.badRequest().body("Autor não encontrado.");
            }

            Comentario comentario = new Comentario();
            comentario.setTexto(texto);
            comentario.setAutor(autor);
            comentario.setPost(post);
            comentario.setDataRegistro(LocalDateTime.now());

            comentarioRepository.save(comentario);

            // Atualiza a lista de comentários do post (opcional, dependendo do mapeamento)
            post.getComentarios().add(comentario);
            postService.save(post);

            return ResponseEntity.ok(comentario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao adicionar comentário: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        Post post = postRepository.findById(id).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (post == null || user == null) {
            return ResponseEntity.badRequest().body("Post ou usuário não encontrado.");
        }
        // Verifica se já existe like desse usuário para esse post
        if (likeRepository.existsByPostAndUser(post, user)) {
            return ResponseEntity.badRequest().body("Usuário já curtiu este post.");
        }
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);

        // Atualiza o contador de likes no post (opcional)
        long likesCount = likeRepository.countByPost(post);
        post.setLikes((int) likesCount);
        postRepository.save(post);

        return ResponseEntity.ok(likesCount);
    }
}