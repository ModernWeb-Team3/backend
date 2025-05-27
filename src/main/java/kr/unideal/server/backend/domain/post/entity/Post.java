package kr.unideal.server.backend.domain.post.entity;

import jakarta.persistence.*;
import kr.unideal.server.backend.domain.category.entity.Category;
import kr.unideal.server.backend.domain.comment.entity.Comment;
import kr.unideal.server.backend.domain.image.entity.Image;
import kr.unideal.server.backend.domain.location.entity.Campus;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.global.entity.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 상품명

    @Column(columnDefinition = "TEXT")
    private String detail; // 설명

    private Integer price; // 가격

    private String status; // 글 상태 (노출/숨김)

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private Campus location;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    public void updatePost(String name, String detail, Integer price, String status) {
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.status = status;
    }

    public void updateStatus(String status) {
        this.status=status;
    }

    public void clearImages() {
        for (Image image : imageList) {
            image.setPost(null); // 연관관계 제거
        }
        imageList.clear();
    }

    public void addImage(Image image) {
        imageList.add(image);
        image.setPost(this);
    }

    public void updatePost(String name, String detail, Integer price, String status, Category category,Campus location) {
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.status = status;
        this.category = category;
        this.location = location;
    }
}
