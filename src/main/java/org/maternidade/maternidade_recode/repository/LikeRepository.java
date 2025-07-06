package org.maternidade.maternidade_recode.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.maternidade.maternidade_recode.model.Like;
import org.maternidade.maternidade_recode.model.Post;
import org.maternidade.maternidade_recode.model.User;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostAndUser(Post post, User user);
    long countByPost(Post post);
}