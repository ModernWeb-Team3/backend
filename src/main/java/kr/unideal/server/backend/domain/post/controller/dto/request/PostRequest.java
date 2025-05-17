package kr.unideal.server.backend.domain.post.controller.dto.request;

import kr.unideal.server.backend.domain.location.entity.LocationType;
import kr.unideal.server.backend.domain.image.controller.dto.request.ImageRequest;
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
    private List<ImageRequest> images; // 수정용 이미지 요청 리스트

    private LocationType location;
}
