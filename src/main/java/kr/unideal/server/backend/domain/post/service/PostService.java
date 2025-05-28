package kr.unideal.server.backend.domain.post.service;

import kr.unideal.server.backend.domain.comment.controller.dto.response.CommentResponse;
import kr.unideal.server.backend.domain.post.entity.Category;
import kr.unideal.server.backend.domain.post.entity.Image;
import kr.unideal.server.backend.domain.location.entity.Campus;
import kr.unideal.server.backend.domain.post.controller.dto.request.ImageRequest;
import kr.unideal.server.backend.domain.post.controller.dto.request.PostRequest;
import kr.unideal.server.backend.domain.post.controller.dto.response.PostListResponse;
import kr.unideal.server.backend.domain.post.controller.dto.response.PostResponse;
import kr.unideal.server.backend.domain.post.entity.Post;
import kr.unideal.server.backend.domain.post.entity.Status;
import kr.unideal.server.backend.domain.post.repository.PostRepository;
import kr.unideal.server.backend.domain.user.entity.User;
import kr.unideal.server.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.unideal.server.backend.domain.post.entity.Status.ON_SALE;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    // post 생성
    public void createPost(PostRequest request, Long userId) {
        Category category = Category.fromDescription(request.getCategory());
        Campus location = Campus.fromDescription(request.getLocation());
        Status status = Status.fromDescription(request.getStatus());
        User user = userService.loaduser(userId);
        Post post = Post.builder()
                .user(user)
                .name(request.getName())
                .detail(request.getDetail())
                .category(category)
                .price(request.getPrice())
                .location(location)
                .status(status)
                .build();

        for (ImageRequest imageReq : request.getImageList()) {
            Image image = Image.builder()
                    .url(imageReq.getUrl())
                    .build();
            post.addImage(image); // 연관관계 자동 설정
        }



        postRepository.save(post); // 이미지도 같이 저장됨       }
    }

    @Transactional
    public PostResponse updatePost(Long userId, Long postId, PostRequest request) {
        Category category = Category.fromDescription(request.getCategory());
        Campus location = Campus.fromDescription(request.getLocation());
        Status status = Status.fromDescription(request.getStatus());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId));

        // 게시글 정보 업데이트
        post.updatePost(request.getName(), request.getDetail(), request.getPrice(), status, category, location);

        // 기존 이미지 제거
        post.clearImages();

        // 새로운 이미지 추가
        for (ImageRequest imageReq : request.getImageList()) {
            Image image = Image.builder()
                    .url(imageReq.getUrl())
                    .build();
            post.addImage(image); // 연관관계 자동 설정
        }

        return convertToResponse(postRepository.save(post));
    }


    // post 삭제
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId);
        }
        postRepository.deleteById(postId);
    }

    // 전체 post 목록 조회
    public List<PostListResponse> getAllPostList() {
        List<Post> posts = postRepository.findAllPostsByStatus(ON_SALE);

        return posts.stream()
                .map(PostListResponse::of)
                .collect(Collectors.toList());
    }
    // 특정 post 상세 조회
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId));
        return convertToResponse(post);
    }

    // 게시글 상태(status) 변경
//    public PostResponse updateStatus(Long postId, Status status) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId));
//        post.updateStatus(status);
//        return convertToResponse(postRepository.save(post));
//    }

    private PostResponse convertToResponse(Post post) {
        List<CommentResponse> commentResponses = post.getCommentList().stream()
                .map(comment -> CommentResponse.from(comment, post.getUser().getName()))
                .collect(Collectors.toList());

        return PostResponse.builder()
                .name(post.getName())
                .detail(post.getDetail())
                .price(post.getPrice())
                .status(post.getStatus().toString())
                .category(post.getCategory().getDescription())
                .location(post.getLocation().getDescription())
                .comments(commentResponses)
                .build();
    }



    public List<PostResponse> getPostsByCategory(String category) {
        Category categoryToString = Category.fromDescription(category);

        List<Post> posts = postRepository.findByCategoryAndStatus(categoryToString, ON_SALE);
        if (posts.isEmpty()) {
            return new ArrayList<>(); // 빈 리스트 반환
        }
        return posts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

}
