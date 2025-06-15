package org.maternidade.maternidade_recode.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@Table(name = "comentario")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;
    private LocalDateTime dataRegistro;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)  // Associando a chave estrangeira para Post
    private Post post;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)  // Associando a chave estrangeira para User
    private User autor;
}
