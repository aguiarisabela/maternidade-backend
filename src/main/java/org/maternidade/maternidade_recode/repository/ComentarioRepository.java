package org.maternidade.maternidade_recode.repository;

import org.maternidade.maternidade_recode.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
}