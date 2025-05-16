package kr.unideal.server.backend.domain.post.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {

    // 이미지 여러장 list로  카테고리는 enum , 상품명, 설명, 거래장소 (enum) , 가격

    private String name;
    private String detail;
    private Integer price;
    private String status;
}