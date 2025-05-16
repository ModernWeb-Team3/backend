package kr.unideal.server.backend.domain.post.repository;

import kr.unideal.server.backend.domain.category.domain.Category;
import kr.unideal.server.backend.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByStatusAndCategoryOrderByCreatedAtDesc(String status, Category category);

}
