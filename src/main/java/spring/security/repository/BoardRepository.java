package spring.security.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.security.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"category", "boardImageList", "commentList"})
    List<Board> findAll();

    @EntityGraph(attributePaths = {"category", "boardImageList", "commentList"})
    Optional<Board> findById(Long id);
}
