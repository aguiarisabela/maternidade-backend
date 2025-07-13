package org.maternidade.maternidade_recode.repository;

import org.maternidade.maternidade_recode.model.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    Page<Comentario> findByPostId(Long postId, Pageable pageable);
    
    @Query("SELECT c FROM Comentario c LEFT JOIN FETCH c.autor WHERE c.id = :id")
    Comentario findByIdWithAutor(Long id);
}