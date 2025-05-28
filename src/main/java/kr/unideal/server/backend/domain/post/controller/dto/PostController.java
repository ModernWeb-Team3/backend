package kr.unideal.server.backend.domain.post.controller.dto;

import kr.unideal.server.backend.domain.post.controller.dto.request.PostRequest;
import kr.unideal.server.backend.domain.post.controller.dto.response.PostListResponse;
import kr.unideal.server.backend.domain.post.controller.dto.response.PostResponse;
import kr.unideal.server.backend.domain.post.entity.Status;
import kr.unideal.server.backend.domain.post.service.PostService;
import kr.unideal.server.backend.domain.user.aop.CurrentUserId;
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
    public ApiResponse<String> createPost(@CurrentUserId Long userId, @RequestBody PostRequest request) {
        System.out.println("User ID: " + userId);
        postService.createPost(request,userId);
        return ApiResponse.ok("게시글이 생성되었습니다.");
    }

    // 게시글을 수정
    @PutMapping("/{postId}")
    public ApiResponse<PostResponse> updatePost(@CurrentUserId Long userId,@PathVariable Long postId, @RequestBody PostRequest request) {
        PostResponse response = postService.updatePost(userId,postId, request);
        return ApiResponse.ok(response);
    }

    // 게시글을 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ApiResponse.ok("게시글이 삭제되었습니다.");
    }

    // 전체 게시글 목록을 조회
    @GetMapping("/all")
    public ApiResponse<List<PostListResponse>> getAllPosts() {
        List<PostListResponse> posts = postService.getAllPostList();
        return ApiResponse.ok(posts);
    }

    // 특정 게시글의 상세 정보를 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse response = postService.getPost(postId);

        return ApiResponse.ok(response);
    }


    // 게시글의 상태(status)를 변경
//    @PatchMapping("/{postId}/status")
//    public ApiResponse<PostResponse> updateStatus(@PathVariable Long postId, @RequestBody Status status) {
//        PostResponse response = postService.updateStatus(postId, status);
//        return ApiResponse.ok(response);
//    }

    //카테고리별 게시글 조회
    @GetMapping
    public ApiResponse<List<PostResponse>> getPostsByCategory(@RequestParam String category) {
        List<PostResponse> posts = postService.getPostsByCategory(category);
        return ApiResponse.ok(posts);
    }


}
