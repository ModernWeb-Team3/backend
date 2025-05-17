// ImageRequest.java
package kr.unideal.server.backend.domain.image.controller.dto.request;

import lombok.Getter;

@Getter
public class ImageRequest {
    private Long id;        // 기존 이미지 ID (없으면 신규)
    private String url;     // 이미지 URL
    private boolean delete; // 삭제 여부
}