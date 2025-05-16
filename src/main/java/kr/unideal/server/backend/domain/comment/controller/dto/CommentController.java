package kr.unideal.server.backend.domain.comment.controller.dto;

import jakarta.validation.Valid;
import kr.unideal.server.backend.domain.comment.controller.dto.request.CommentRequest;
import kr.unideal.server.backend.domain.comment.controller.dto.request.CommentUpdatedRequest;
import kr.unideal.server.backend.domain.comment.service.CommentService;
import kr.unideal.server.backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 특정 post의 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    public ApiResponse<String> createComment(@PathVariable Long postId,
                                           @Valid @RequestBody CommentRequest request) {
        Long userId = 3L; // 임시 하드코딩
        commentService.registerComment(request, postId, userId);
        return ApiResponse.ok("댓글이 등록되었습니다.");
    }

    // 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ApiResponse<String> updateComment(@PathVariable Long commentId,
                                           @RequestBody CommentUpdatedRequest request) {
        Long userId = 3L; // 임시 하드코딩
        commentService.updateComment(userId, commentId, request);
        return ApiResponse.ok("댓글이 수정되었습니다.");
    }

    // 댓글 삭제
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ApiResponse<String> deleteComment(@PathVariable Long postId,
                                           @PathVariable Long commentId) {
        Long userId = 3L;
        commentService.deleteComment(userId, commentId);
        return ApiResponse.ok("댓글이 삭제되었습니다.");
    }

    // 댓글 비밀글 설정
    @PatchMapping("/comments/{commentId}/privates")
    public ApiResponse<String> setCommentPrivate(@PathVariable Long commentId) {
        // 구현 생략
        return ApiResponse.ok("비밀글로 설정되었습니다.");
    }

    // 대댓글 작성
    @PostMapping("/comments/{commentId}/replies")
    public ApiResponse<String> createReply(@PathVariable Long commentId,
                                         @RequestBody CommentRequest request) {
        Long userId = 3L;
        commentService.registerReply(request, commentId, userId);
        return ApiResponse.ok("대댓글이 등록되었습니다.");
    }

    // 대댓글 수정
    @PatchMapping("/comments/{commentId}/replies")
    public ApiResponse<String> updateReply(@PathVariable Long commentId,
                                         @RequestBody CommentUpdatedRequest request) {
        Long userId = 3L;
        commentService.updateReply(userId, commentId, request);
        return ApiResponse.ok("대댓글이 수정되었습니다.");
    }

    // 대댓글 삭제
    @DeleteMapping("/comments/{commentId}/replies")
    public ApiResponse<String> deleteReply(@PathVariable Long commentId) {
        Long userId = 3L;
        commentService.deleteComment(userId, commentId);
        return ApiResponse.ok("대댓글이 삭제되었습니다.");
    }
}
