package kr.unideal.server.backend.domain.post.controller;

import kr.unideal.server.backend.domain.post.controller.dto.request.PostRequest;
import kr.unideal.server.backend.domain.post.controller.dto.response.PostResponse;
import kr.unideal.server.backend.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // status = "노출"인 경우만 최신 순으로 조회
    @GetMapping  // 실제 URL: /posts
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    // 🔹 GET /posts/{postId} - 단건 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    // 🔹 GET /posts/category/{categoryId} - 카테고리별 게시글 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostResponse>> getPostsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(postService.getPostsByCategory(categoryId));
    }


    // 🔹 POST /posts - 생성
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest request) {
        postService.createPost(request);
        return ResponseEntity.ok().build();
    }

    // 🔹 PUT /posts/{postId} - 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.updatePost(postId, request));
    }

    // 🔹 DELETE /posts/{postId} - 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    // 🔹 PATCH /posts/{postId}/status - 상태만 수정
    @PatchMapping("/{postId}/status")
    public ResponseEntity<PostResponse> updateStatus(@PathVariable Long postId, @RequestBody String status) {
        return ResponseEntity.ok(postService.updateStatus(postId, status));
    }

    //    // 🔹 댓글 관련은 별도 CommentController로 분리 권장
    //    @GetMapping("/{postId}/comments")
    //    public String getComments(@PathVariable Long postId) {
    //        return "댓글 리스트 조회 기능은 구현 예정";
    //    }
    //
    //    @PostMapping("/{postId}/comments")
    //    public String createComment(@PathVariable Long postId) {
    //        return "댓글 생성 기능은 구현 예정";
    //    }
    //
    //    @DeleteMapping("/{postId}/comments/{commentId}/delete")
    //    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
    //        return "댓글 삭제 기능은 구현 예정";
    //    }

    // 내부 변환 -> toDto -> PostResponse 에 추가
    //    private PostResponse convertToResponse(Post post) {
    //        return PostResponse.builder()
    //                .id(post.getId())
    //                .name(post.getName())
    //                .detail(post.getDetail())
    //                .price(post.getPrice())
    //                .status(post.getStatus())
    //                .build();
    //    }
}