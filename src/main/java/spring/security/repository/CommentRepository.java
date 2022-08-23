package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
