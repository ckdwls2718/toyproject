package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
