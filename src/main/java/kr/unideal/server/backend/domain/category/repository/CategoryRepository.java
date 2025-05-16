package kr.unideal.server.backend.domain.category.repository;

import kr.unideal.server.backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 필요 시 이름으로 조회 가능
    Category findByName(String name);
}
