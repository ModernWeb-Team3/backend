package kr.unideal.server.backend.domain.post.repository;

import kr.unideal.server.backend.domain.post.entity.Category;
import kr.unideal.server.backend.domain.post.entity.Post;
import kr.unideal.server.backend.domain.post.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.category = :category AND p.status = :status ORDER BY p.createdAt DESC")
    List<Post> findByCategoryAndStatus(@Param("category") Category category, @Param("status") Status status);

    @Query("SELECT p FROM Post p WHERE p.status = :status ORDER BY p.createdAt DESC")
    List<Post> findAllPostsByStatus(@Param("status") Status status);

    @Query("SELECT p FROM Post p WHERE p.name LIKE %:keyword% AND p.status = :status ORDER BY p.createdAt DESC")
    List<Post> findByNameContainingAndStatus(String keyword, Status status);
}
