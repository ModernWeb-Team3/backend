package kr.unideal.server.backend.domain.post.domain;

import jakarta.persistence.*;
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



    /*
     * // 추후 확장용 연관관계 주석 처리 (원할 때 주석 해제해서 사용 가능)
     *
     * @ManyToOne
     *
     * @JoinColumn(name = "user_id")
     * private User user;
     *
     * @ManyToOne
     *
     * @JoinColumn(name = "category_id")
     * private Category category;
     *
     * @ManyToOne
     *
     * @JoinColumn(name = "location_id")
     * private Location location;
     */
}
