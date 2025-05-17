package kr.unideal.server.backend.domain.post.controller.dto.response;

import kr.unideal.server.backend.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponse {

    //
    private Long id;
    private String name;     // 상품명
    private String detail;   // 설명
    private Integer price;   // 가격
    private String status;   // 상태 (노출/숨김)

    //toDto
    private String category; // 카테고리 이름

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .name(post.getName())
                .detail(post.getDetail())
                .price(post.getPrice())
                .status(post.getStatus())
                .category(post.getCategory() != null ? post.getCategory().getName() : null)
                .build();
    }
}
