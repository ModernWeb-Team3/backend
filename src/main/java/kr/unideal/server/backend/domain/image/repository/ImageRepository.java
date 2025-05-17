package kr.unideal.server.backend.domain.image.repository;

import kr.unideal.server.backend.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}