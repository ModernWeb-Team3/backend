package kr.unideal.server.backend.domain.post.service;

import kr.unideal.server.backend.domain.post.controller.dto.request.PostRequest;
import kr.unideal.server.backend.domain.post.controller.dto.response.PostResponse;
import kr.unideal.server.backend.domain.post.entity.Post;
import kr.unideal.server.backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // post 생성
    public void createPost(PostRequest request) {
        Post post = Post.builder()
                .name(request.getName())
                .detail(request.getDetail())
                .price(request.getPrice())
                .status(request.getStatus())
                .build();
        postRepository.save(post);
    }

    // post 수정
    public PostResponse updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId));
        post.updatePost(request.getName(), request.getDetail(), request.getPrice(), request.getStatus());
        return convertToResponse(postRepository.save(post));
    }

    // post 삭제
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId);
        }
        postRepository.deleteById(postId);
    }
//
//     전체 post 목록 조회 -> 수정 필요
//        public List<PostResponse> getAllPosts() {
//            return postRepository.findByStatusAndCategoryOrderByCreatedAtDesc("노출", null)
//                    .stream()
//                    .map(this::convertToResponse)
//                    .collect(Collectors.toList());
//        }

    public List<PostResponse> getAllPosts() {
        return postRepository.findByStatusOrderByCreatedAtDesc("노출")
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }




    // 특정 post 상세 조회
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId));
        return convertToResponse(post);
    }

    // 게시글 상태(status) 변경
    public PostResponse updateStatus(Long postId, String status) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId));
        post.updateStatus(status);
        return convertToResponse(postRepository.save(post));
    }

    private PostResponse convertToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .name(post.getName())
                .detail(post.getDetail())
                .price(post.getPrice())
                .status(post.getStatus())
                .build();
    }
}
