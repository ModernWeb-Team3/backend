package kr.unideal.server.backend.domain.comment.controller.dto.response;

import kr.unideal.server.backend.domain.comment.domain.Comment;


public record CommentResponse(

        Long commentId,
        String name,
        String content,
        boolean isPrivate
) {
    public static CommentResponse from(Comment comment, String name) {
        return new CommentResponse(
                comment.getId(),
                name,
                comment.isSecret() ? "(비밀글입니다)" : comment.getContent(),
                comment.isSecret()

        );
    }
}
