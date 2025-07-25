package kr.unideal.server.backend.domain.post.controller.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PostRequest {

    private String name;
    private String detail;
    private Integer price;
    private String status;
    private String category;
    private String location;
    private List<ImageRequest> imageList;

}
