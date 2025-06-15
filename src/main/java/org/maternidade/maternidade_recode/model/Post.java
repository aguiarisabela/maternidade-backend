package org.maternidade.maternidade_recode.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Post") // Nome da tabela no banco
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "conteudo")
    private String conteudo;

    @Column(name = "dataCriacao")
    private LocalDateTime dataCriacao;

    @ManyToOne // Relacionamento: um post tem um autor
    @JoinColumn(name = "autorId") // Nome da coluna no banco
    private User autor;

    // Construtor vazio (necessário para JPA)
    public Post() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public User getAutor() {
        return autor;
    }

    public void setAutor(User autor) {
        this.autor = autor;
    }
}