package kr.unideal.server.backend.domain.post.service;

import kr.unideal.server.backend.domain.post.controller.dto.request.PostRequest;
import kr.unideal.server.backend.domain.post.controller.dto.response.PostResponse;
import kr.unideal.server.backend.domain.post.entity.Post;
import kr.unideal.server.backend.domain.category.entity.Category;
import kr.unideal.server.backend.domain.image.entity.Image;
import kr.unideal.server.backend.domain.post.repository.PostRepository;
import kr.unideal.server.backend.domain.category.repository.CategoryRepository;
import kr.unideal.server.backend.domain.image.repository.ImageRepository;
import kr.unideal.server.backend.domain.image.controller.dto.request.ImageRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;


    private final ImageRepository imageRepository;  // 주입 추가

    // post 생성
    public void createPost(PostRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        Post post = Post.builder()
                .name(request.getName())
                .detail(request.getDetail())
                .price(request.getPrice())
                .status(request.getStatus())
                .category(category) // 여기에 실제 Category 객체 연결
                .build();

        postRepository.save(post);

        List<String> urls = request.getImageUrls();
        if (urls != null && urls.size() <= 3) {
            List<Image> images = urls.stream()
                    .map(url -> Image.builder()
                            .url(url)
                            .post(post)
                            .build())
                    .toList();
            imageRepository.saveAll(images);
        } else if (urls != null) {
            throw new IllegalArgumentException("이미지는 최대 3장까지만 등록 가능합니다.");
        }
    }

    // post 수정
    public PostResponse updatePost(Long postId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + postId));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // 기본 정보 업데이트
        post.updatePost(request.getName(), request.getDetail(), request.getPrice(), request.getStatus());
        post.setCategory(category);

        // 이미지 업데이트 로직 추가
        List<ImageRequest> imageRequests = request.getImages();
        if (imageRequests != null) {

            // 삭제할 이미지 찾기
            List<Image> toDelete = post.getImages().stream()
                    .filter(existing -> imageRequests.stream()
                            .anyMatch(req -> req.getId() != null && req.isDelete() && req.getId().equals(existing.getId())))
                    .collect(Collectors.toList());
            post.getImages().removeAll(toDelete);  // 삭제 적용됨 (orphanRemoval = true)

            // 새 이미지 추가
            List<Image> toAdd = imageRequests.stream()
                    .filter(req -> req.getId() == null && !req.isDelete())
                    .map(req -> Image.builder()
                            .url(req.getUrl())
                            .post(post)
                            .build())
                    .collect(Collectors.toList());
            post.getImages().addAll(toAdd);
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

    // 특정 카테고리에 해당하는 게시글 조회
    // PostService.java

    public List<PostResponse> getPostsByCategory(Long categoryId) {
        return postRepository.findByCategoryIdAndStatusOrderByCreatedAtDesc(categoryId, "노출")
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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
                .category(post.getCategory() != null ? post.getCategory().getName() : null)
                .imageUrls(post.getImages() != null
                        ? post.getImages().stream()
                        .map(Image::getUrl)
                        .collect(Collectors.toList())
                        : new ArrayList<>()) // null 방지
                .build();
    }
}
