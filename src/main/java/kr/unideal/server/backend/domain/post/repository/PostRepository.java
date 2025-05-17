package kr.unideal.server.backend.domain.post.repository;

import kr.unideal.server.backend.domain.category.entity.Category;
import kr.unideal.server.backend.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findByStatusAndCategoryOrderByCreatedAtDesc(String status, Category category);
    List<Post> findByStatusOrderByCreatedAtDesc(String status);

    // 특정 카테고리 ID에 해당하는 게시글 목록 조회
    // 카테고리 + 노출 상태
    List<Post> findByCategoryIdAndStatusOrderByCreatedAtDesc(Long categoryId, String status);

}