package kr.unideal.server.backend.domain.post.controller.dto;

import kr.unideal.server.backend.domain.post.controller.dto.request.PostRequest;
import kr.unideal.server.backend.domain.post.controller.dto.response.PostResponse;
import kr.unideal.server.backend.domain.post.service.PostService;
import kr.unideal.server.backend.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 새로운 게시글을 생성 -
    @PostMapping
    public ApiResponse<String> createPost(@RequestBody PostRequest request) {
        postService.createPost(request);
        return ApiResponse.ok("게시글이 생성되었습니다.");
    }

    // 게시글을 수정
    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequest request) {
        PostResponse response = postService.updatePost(postId, request);
        return ApiResponse.ok(response);
    }

    // 게시글을 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ApiResponse.ok("게시글이 삭제되었습니다.");
    }

    // 전체 게시글 목록을 조회
    @GetMapping
    public ApiResponse<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ApiResponse.ok(posts);
    }

    // 특정 게시글의 상세 정보를 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse response = postService.getPost(postId);
        return ApiResponse.ok(response);
    }



    // 게시글의 상태(status)를 변경
    @PatchMapping("/{postId}/status")
    public ApiResponse<PostResponse> updateStatus(@PathVariable Long postId, @RequestBody String status) {
        PostResponse response = postService.updateStatus(postId, status);
        return ApiResponse.ok(response);
    }
}
