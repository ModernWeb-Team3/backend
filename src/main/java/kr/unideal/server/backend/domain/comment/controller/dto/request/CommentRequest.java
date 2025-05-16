package kr.unideal.server.backend.domain.comment.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotBlank(message = "이름은 비워둘 수 없습니다.")
        String name,

        @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
        String content,

        @NotNull(message = "비밀 여부는 null일 수 없습니다.")
        Boolean isPrivate,

        Long parentId
) {}
