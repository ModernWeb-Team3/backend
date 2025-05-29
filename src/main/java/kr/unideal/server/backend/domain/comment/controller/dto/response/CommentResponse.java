package kr.unideal.server.backend.domain.comment.controller.dto.response;

import kr.unideal.server.backend.domain.comment.entity.Comment;


public record CommentResponse(
        String name,
        String content,
        boolean isPrivate,
        Long parentId
) {
    public static CommentResponse from(Comment comment, String name) {
        return new CommentResponse(
                name,
                comment.isSecret() ? "(비밀글입니다)" : comment.getContent(),
                comment.isSecret()
                , comment.getParent() != null ? comment.getParent().getId() : null

        );
    }
}
