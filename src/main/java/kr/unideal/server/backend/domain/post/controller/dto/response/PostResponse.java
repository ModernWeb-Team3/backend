package kr.unideal.server.backend.domain.post.controller.dto.response;

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

}
