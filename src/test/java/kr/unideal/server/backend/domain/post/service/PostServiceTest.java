package kr.unideal.server.backend.domain.post.service;

import kr.unideal.server.backend.domain.location.entity.Campus;
import kr.unideal.server.backend.domain.post.controller.dto.request.ImageRequest;
import kr.unideal.server.backend.domain.post.controller.dto.request.PostRequest;
import kr.unideal.server.backend.domain.post.entity.*;
import kr.unideal.server.backend.domain.post.repository.PostRepository;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost_성공() {
        // given
        Long userId = 1L;
        User mockUser = User.builder().id(userId).name("홍길동").build();

        PostRequest request = PostRequest.builder()
                .name("노트북")
                .detail("사용감 거의 없음")
                .price(100000)
                .category("전자기기")
                .location("가천관")
                .status("판매중")
                .imageList(List.of(ImageRequest.builder().url("http://img.com/1.jpg").build()))
                .build();

        when(userService.loaduser(userId)).thenReturn(mockUser);

        // when
        postService.createPost(request, userId);

        // then
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void updatePost_성공() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        Post post = Post.builder()
                .id(postId)
                .name("구노트북")
                .detail("좀 사용함")
                .price(50000)
                .status(Status.ON_SALE)
                .category(Category.ELECTRONICS)
                .location(Campus.GACHON)
                .user(User.builder().id(userId).name("홍길동").build())
                .build();

        PostRequest updateRequest = PostRequest.builder()
                .name("새노트북")
                .detail("거의 새제품")
                .price(150000)
                .status("판매중")
                .category("전자기기")
                .location("가천관")
                .imageList(List.of(ImageRequest.builder().url("http://img.com/new.jpg").build()))
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // when
        var response = postService.updatePost(userId, postId, updateRequest);

        // then
        assertThat(response.getName()).isEqualTo("새노트북");
        assertThat(response.getImageList()).hasSize(1);
    }

    @Test
    void getAllPostList_성공() {
        // given
        Post post1 = Post.builder()
                .name("물건1")
                .status(Status.ON_SALE)
                .location(Campus.GACHON)
                .price(1000)
                .build();

        Post post2 = Post.builder()
                .name("물건2")
                .status(Status.ON_SALE)
                .location(Campus.GACHON)
                .price(2000)
                .build();

        when(postRepository.findAllPostsByStatus(Status.ON_SALE)).thenReturn(List.of(post1, post2));

        // when
        var result = postService.getAllPostList();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void getPost_성공() {
        // given
        Long postId = 1L;
        Post post = Post.builder()
                .id(postId)
                .name("자전거")
                .detail("중고")
                .price(70000)
                .status(Status.ON_SALE)
                .category(Category.BOOK)
                .location(Campus.GACHON)
                .user(User.builder().name("홍길동").build())
                .imageList(List.of(Image.builder().url("http://img.com/bike.jpg").build()))
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        var result = postService.getPost(postId);

        // then
        assertThat(result.getName()).isEqualTo("자전거");
        assertThat(result.getImageList()).hasSize(1);
    }
}
