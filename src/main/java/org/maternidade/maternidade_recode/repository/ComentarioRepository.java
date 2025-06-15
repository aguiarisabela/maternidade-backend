package org.maternidade.maternidade_recode.repository;

import java.util.List;

import org.maternidade.maternidade_recode.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByPostId(Long postId);
}