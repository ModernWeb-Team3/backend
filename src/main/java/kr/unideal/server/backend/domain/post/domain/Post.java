package kr.unideal.server.backend.domain.post.domain;

import jakarta.persistence.*;
import kr.unideal.server.backend.domain.category.domain.Category;
import kr.unideal.server.backend.domain.user.domain.User;
import kr.unideal.server.backend.global.common.BaseTimeEntity;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void updatePost(String name, String detail, Integer price, String status) {
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.status = status;
    }

    public void updateStatus(String status) {
        this.status=status;
    }

//    @ManyToOne
//    @JoinColumn(name = "location_id")
//     private Location location;

}
