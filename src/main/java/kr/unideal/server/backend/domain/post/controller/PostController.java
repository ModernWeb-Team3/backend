package kr.unideal.server.backend.domain.post.controller;

import kr.unideal.server.backend.domain.post.dto.request.PostRequest;
import kr.unideal.server.backend.domain.post.dto.response.PostResponse;
import kr.unideal.server.backend.domain.post.entity.Post;
import kr.unideal.server.backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;

    // status = "노출"인 경우만 최신 순으로 조회

    @GetMapping  // 실제 URL: /posts
    public List<PostResponse> getAllPosts() {
        return postRepository.findByStatusOrderByCreatedAtDesc("노출").stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 🔹 GET /posts/{postId} - 단건 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return postRepository.findById(postId)
                .map(post -> ResponseEntity.ok(convertToResponse(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 POST /posts - 생성
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest request) {
        Post post = Post.builder()
                .name(request.getName())
                .detail(request.getDetail())
                .price(request.getPrice())
                .status(request.getStatus())
                .build();
        return ResponseEntity.ok(convertToResponse(postRepository.save(post)));
    }

    // 🔹 PUT /posts/{postId} - 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequest request) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) return ResponseEntity.notFound().build();

        Post post = optionalPost.get();
        post.setName(request.getName());
        post.setDetail(request.getDetail());
        post.setPrice(request.getPrice());
        post.setStatus(request.getStatus());
        return ResponseEntity.ok(convertToResponse(postRepository.save(post)));
    }

    // 🔹 DELETE /posts/{postId} - 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        if (!postRepository.existsById(postId)) return ResponseEntity.notFound().build();
        postRepository.deleteById(postId);
        return ResponseEntity.noContent().build();
    }

    // 🔹 PATCH /posts/{postId}/status - 상태만 수정
    @PatchMapping("/{postId}/status")
    public ResponseEntity<PostResponse> updateStatus(@PathVariable Long postId, @RequestBody String status) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isEmpty()) return ResponseEntity.notFound().build();

        Post post = optionalPost.get();
        post.setStatus(status);
        return ResponseEntity.ok(convertToResponse(postRepository.save(post)));
    }

    // 🔹 댓글 관련은 별도 CommentController로 분리 권장
    @GetMapping("/{postId}/comments")
    public String getComments(@PathVariable Long postId) {
        return "댓글 리스트 조회 기능은 구현 예정";
    }

    @PostMapping("/{postId}/comments")
    public String createComment(@PathVariable Long postId) {
        return "댓글 생성 기능은 구현 예정";
    }

    @DeleteMapping("/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        return "댓글 삭제 기능은 구현 예정";
    }

    // 내부 변환
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