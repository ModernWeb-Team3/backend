package kr.unideal.server.backend.domain.comment.service;

import kr.unideal.server.backend.domain.comment.controller.dto.request.CommentRequest;
import kr.unideal.server.backend.domain.comment.controller.dto.request.CommentUpdatedRequest;
import kr.unideal.server.backend.domain.comment.entity.Comment;
import kr.unideal.server.backend.domain.comment.repository.CommentRepository;
import kr.unideal.server.backend.domain.post.entity.Post;
import kr.unideal.server.backend.domain.post.repository.PostRepository;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.domain.user.repository.UserRepository;
import kr.unideal.server.backend.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).name("user1").build();
        post = Post.builder().id(1L).build();
        comment = Comment.builder().id(1L).user(user).post(post).content("test").build();
    }

    @Test
    void registerComment_success() {
        CommentRequest request = new CommentRequest("test content", false, null);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        commentService.registerComment(request, 1L, 1L);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void updateComment_success() {
        CommentUpdatedRequest request = new CommentUpdatedRequest("updated content", true);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.updateComment(1L, 1L, request);

        assertThat(comment.getContent()).isEqualTo("updated content");
        assertThat(comment.isSecret()).isTrue();
    }

    @Test
    void deleteComment_success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteComment(1L, 1L);

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void registerReply_success() {
        CommentRequest request = new CommentRequest("reply content", true, null);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        commentService.registerReply(request, 1L, 1L);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void getComments_success() {
        when(commentRepository.findAllByPostId(1L)).thenReturn(List.of(comment));

        var result = commentService.getComments(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).content()).isEqualTo("test");
    }

    @Test
    void getComments_empty_throwsException() {
        when(commentRepository.findAllByPostId(1L)).thenReturn(Collections.emptyList());

        assertThrows(CustomException.class, () -> commentService.getComments(1L));
    }

    @Test
    void setCommentSecret_success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        boolean result = commentService.setCommentSecret(1L, 1L);

        assertThat(result).isTrue();
        assertThat(comment.isSecret()).isTrue();
    }

    @Test
    void setCommentSecret_accessDenied_throwsException() {
        User anotherUser = User.builder().id(2L).build();
        Comment otherComment = Comment.builder().id(1L).user(anotherUser).build();

        when(commentRepository.findById(1L)).thenReturn(Optional.of(otherComment));

        assertThrows(CustomException.class, () -> commentService.setCommentSecret(1L, 1L));
    }
}
