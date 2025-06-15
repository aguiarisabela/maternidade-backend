package org.maternidade.maternidade_recode.controller;

import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;
// import org.maternidade.maternidade_recode.model.User;
import org.maternidade.maternidade_recode.service.PostService;
import org.maternidade.maternidade_recode.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
// import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public String listPosts(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts/list";
    }

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("users", userService.findAll());
        return "posts/form";
    }

    @PostMapping
    public String savePost(@ModelAttribute Post post) {
        post.setDataCriacao(LocalDateTime.now());

        // Buscar o usuário pelo ID e associá-lo ao post
        User autor = userService.findById(post.getAutor().getId());
        post.setAutor(autor);

        postService.save(post);
        return "redirect:/posts";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        model.addAttribute("users", userService.findAll());
        return "posts/form";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/posts";
    }
}