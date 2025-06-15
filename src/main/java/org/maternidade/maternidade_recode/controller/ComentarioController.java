package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.Comentario;
import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.ComentarioService;
import org.maternidade.maternidade_recode.service.PostService;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioRepository;
    private final PostService postService;
    private final UserService userService;

    public ComentarioController(ComentarioService comentarioRepository, PostService postService, UserService userService) {
        this.comentarioRepository = comentarioRepository;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String listComentario(Model model) {
        model.addAttribute("comentarios", comentarioRepository.findAll());
        return "comentarios/list";
    }

    @GetMapping("/new")
    public String newComentarioForm(Model model) {
        model.addAttribute("comentario", new Comentario());
        model.addAttribute("posts", postService.findAll());
        model.addAttribute("users", userService.findAll());
        return "comentarios/form";
    }

    @PostMapping
    public String saveComentario(@ModelAttribute Comentario comentario) {
        // Carrega o post e o usuário associados, usando os IDs enviados no formulário
        Post post = postService.findById(comentario.getPost().getId());
        User autor = userService.findById(comentario.getAutor().getId());

        // Atribui o post e o autor ao comentário
        comentario.setPost(post);
        comentario.setAutor(autor);

        // Define a data de criação
        comentario.setDataRegistro(LocalDateTime.now());

        // Salva o comentário
        comentarioRepository.save(comentario);

        // Redireciona para a lista de comentários
        return "redirect:/comentarios";
    }

    @GetMapping("/edit/{id}")
    public String editComentario(@PathVariable Long id, Model model) {
        Comentario comentario = comentarioRepository.findById(id);
        model.addAttribute("comentario", comentario);
        model.addAttribute("posts", postService.findAll());
        model.addAttribute("users", userService.findAll());
        return "comentarios/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteComentario(@PathVariable Long id) {
        comentarioRepository.delete(id);
        return "redirect:/comentarios";
    }
}