package org.maternidade.maternidade_recode.repository;

import org.maternidade.maternidade_recode.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.autor ORDER BY p.dataCriacao DESC")
    List<Post> findAllWithAutor();
}