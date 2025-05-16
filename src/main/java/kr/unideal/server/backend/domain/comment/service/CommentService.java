package kr.unideal.server.backend.domain.comment.service;

import jakarta.validation.Valid;
import kr.unideal.server.backend.domain.comment.controller.dto.request.CommentRequest;
import kr.unideal.server.backend.domain.comment.controller.dto.request.CommentUpdatedRequest;
import kr.unideal.server.backend.domain.comment.controller.dto.response.CommentResponse;
import kr.unideal.server.backend.domain.comment.entity.Comment;
import kr.unideal.server.backend.domain.comment.repository.CommentRepository;
import kr.unideal.server.backend.domain.post.entity.Post;
import kr.unideal.server.backend.domain.post.repository.PostRepository;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.domain.user.repository.UserRepository;
import kr.unideal.server.backend.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static kr.unideal.server.backend.global.exception.ErrorCode.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // 댓글 등록
    @Transactional
    public void registerComment(@Valid CommentRequest request, Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        Comment parentComment = null;
        if (request.parentId() != null) {
            parentComment = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        }
        Comment comment = Comment.of(user, request.content(), post, parentComment);
        commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long userId, Long commentId, CommentUpdatedRequest request) {
        Comment comment = getAuthorizedComment(userId, commentId);
        comment.updateContent(request.content());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = getAuthorizedComment(userId, commentId);
        commentRepository.delete(comment);
    }

    // 대댓글 등록
    @Transactional
    public void registerReply(CommentRequest request, Long parentCommentId, Long userId) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        Post post = parentComment.getPost();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Comment reply = Comment.of(user, request.content(), post, parentComment);
        commentRepository.save(reply);
    }

    // 대댓글 수정
    @Transactional
    public void updateReply(Long userId, Long commentId, CommentUpdatedRequest request) {
        updateComment(userId, commentId, request); // 동일 로직
    }

    // 댓글 전체 조회
    @Transactional(readOnly = true)
    public List<CommentResponse> getAllCommentsByPostId(Long postId) {
        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        return commentList.stream()
                .map(comment -> CommentResponse.from(comment, comment.getUser().getName()))
                .toList();
    }

    // 권한 확인
    private Comment getAuthorizedComment(Long userId, Long commentId) {
        Comment comment = getCommentsByCommentId(commentId);
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ACCESS_DENY);
        }
        return comment;
    }

    // commentId로 조회
    public Comment getCommentsByCommentId(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
    }
}
