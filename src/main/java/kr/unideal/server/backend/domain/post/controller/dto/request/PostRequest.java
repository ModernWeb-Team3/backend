package kr.unideal.server.backend.domain.post.controller.dto.request;

import lombok.Getter;
import java.util.List;

@Getter
public class PostRequest {

    // 이미지 여러장 list로  카테고리는 enum , 상품명, 설명, 거래장소 (enum) , 가격

    private String name;
    private String detail;
    private Integer price;
    private String status;
    private Long categoryId;

    private List<String> imageUrls; // 추가
}
