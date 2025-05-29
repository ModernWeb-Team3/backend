package kr.unideal.server.backend.domain.post.controller.dto.response;

import kr.unideal.server.backend.domain.post.entity.Post;
import lombok.Builder;

@Builder
public record PostListResponse(


        String thumbnail, // 썸네일 이미지 URL
        String name,     // 상품명
        String location,
        int price,   // 가격
        String status // 판매 여부
) {
    public static PostListResponse of(Post post) {
        String thumbnail = post.getImageList().isEmpty()
                ? "default-thumbnail.jpg" // 기본 썸네일 경로
                : post.getImageList().get(0).getUrl();

        return PostListResponse.builder()
                .thumbnail(thumbnail)
                .name(post.getName())
                .location(post.getLocation().getDescription())
                .price(post.getPrice())
                .status(String.valueOf(post.getStatus()))
                .build();
    }

}
