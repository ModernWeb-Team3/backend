package kr.unideal.server.backend.domain.post.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageResponse {
    private String url; // 이미지 URL

    @Override
    public String toString() {
        return "ImageResponse{" +
                "url='" + url + '\'' +
                '}';
    }

}
