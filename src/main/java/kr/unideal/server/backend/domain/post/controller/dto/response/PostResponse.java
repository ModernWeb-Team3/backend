package kr.unideal.server.backend.domain.post.controller.dto.response;

import kr.unideal.server.backend.domain.comment.controller.dto.response.CommentResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostResponse {
    private String name;     // 상품명
    private String detail;   // 설명
    private Integer price;   // 가격
    private String status;   // 상태 (노출/숨김)
    private String category;
    private String location;
    private List<CommentResponse> comments;

}
