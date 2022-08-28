package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.security.entity.EpilogueImage;

public interface EpilogueImageRepository extends JpaRepository<EpilogueImage, Long> {
}
