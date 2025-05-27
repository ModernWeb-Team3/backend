package kr.unideal.server.backend.domain.post.repository;

import kr.unideal.server.backend.domain.category.entity.Category;
import kr.unideal.server.backend.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.status = :status ORDER BY p.createdAt DESC")
    List<Post> findByCategoryAndStatus(@Param("category") Category category, @Param("status") String status);

}
