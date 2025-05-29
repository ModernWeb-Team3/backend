package kr.unideal.server.backend.domain.comment.controller.dto;

import jakarta.validation.Valid;
import kr.unideal.server.backend.domain.comment.controller.dto.request.CommentRequest;
import kr.unideal.server.backend.domain.comment.controller.dto.request.CommentUpdatedRequest;
import kr.unideal.server.backend.domain.comment.service.CommentService;
import kr.unideal.server.backend.domain.user.aop.CurrentUserId;
import kr.unideal.server.backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{postId}/comments")
    public ApiResponse<String> createComment(@CurrentUserId Long userId, @PathVariable Long postId,
                                             @Valid @RequestBody CommentRequest request) {
        commentService.registerComment(request, postId, userId);
        return ApiResponse.ok("댓글이 등록되었습니다.");
    }

    // 대댓글 작성
    @PostMapping("/comments/{commentId}/replies")
    public ApiResponse<String> createReply(@CurrentUserId Long userId,
                                           @PathVariable Long commentId,
                                           @RequestBody CommentRequest request) {
        commentService.registerReply(request, commentId, userId);
        return ApiResponse.ok("대댓글이 등록되었습니다.");
    }

    // 특정 게시글의 댓글 목록을 조회
    @GetMapping("/{postId}/comments")
    public ApiResponse<?> getComments(@PathVariable Long postId) {
        return ApiResponse.ok(commentService.getComments(postId));
    }


    // 비밀글 토글
    @PatchMapping("/comments/{commentId}/private")
    public ApiResponse<String> toggleCommentPrivate(@CurrentUserId Long userId,
                                                    @PathVariable Long commentId) {
        boolean newSecretStatus = commentService.setCommentSecret(userId, commentId);
        String message = newSecretStatus ? "비밀글로 설정되었습니다." : "비밀글 해제되었습니다.";
        return ApiResponse.ok(message);
    }


    // 댓글 수정
    @PatchMapping("comments/{commentId}")
    public ApiResponse<String> updateComment(@CurrentUserId Long userId,
                                             @PathVariable Long commentId,
                                             @RequestBody CommentUpdatedRequest request) {
        commentService.updateComment(userId, commentId, request);
        return ApiResponse.ok("댓글이 수정되었습니다.");
    }

    // 댓글 삭제
    @DeleteMapping("comments/{commentId}")
    public ApiResponse<String> deleteComment(@CurrentUserId Long userId,
                                             @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        return ApiResponse.ok("댓글이 삭제되었습니다.");
    }

}
