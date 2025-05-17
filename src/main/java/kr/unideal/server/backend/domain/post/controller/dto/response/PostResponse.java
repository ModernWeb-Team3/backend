package kr.unideal.server.backend.domain.post.controller.dto.response;

import kr.unideal.server.backend.domain.image.entity.Image;
import kr.unideal.server.backend.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostResponse {

    //
    private Long id;
    private String name;     // 상품명
    private String detail;   // 설명
    private Integer price;   // 가격
    private String status;   // 상태 (노출/숨김)
    private String category; // 카테고리 이름
    private List<String> imageUrls;  //  추가

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .name(post.getName())
                .detail(post.getDetail())
                .price(post.getPrice())
                .status(post.getStatus())
                .category(post.getCategory() != null ? post.getCategory().getName() : null)
                .imageUrls(post.getImages() != null ?
                        post.getImages().stream()
                                .map(Image::getUrl)
                                .collect(Collectors.toList())
                        : null)  //  이미지 URL 리스트 포함
                .build();
    }
}
