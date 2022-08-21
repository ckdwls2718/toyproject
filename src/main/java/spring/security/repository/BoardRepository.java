package spring.security.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"category", "boardImageList"})
    List<Board> findAll();

    @EntityGraph(attributePaths = {"category", "boardImageList"})
    Optional<Board> findById(Long id);
}
