package com.junghoon.studylog.controller.post;

import com.junghoon.studylog.domain.post.PostResponse;
import com.junghoon.studylog.dto.post.PostCreateRequest;
import com.junghoon.studylog.dto.post.PostUpdateRequest;
import com.junghoon.studylog.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    /**
     * 공부/독서 기록 작성
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();

        PostResponse response = postService.create(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 내 기록 목록 조회
     */
    @GetMapping("/me")
    public ResponseEntity<List<PostResponse>> getMyPosts(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();

        List<PostResponse> posts = postService.getMyPosts(email);
        return ResponseEntity.ok(posts);
    }

    /**
     * 게시글 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        PostResponse response = postService.getPost(id, email);
        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        PostResponse response = postService.update(id, request, email);
        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        postService.delete(id, email);
        return ResponseEntity.noContent().build(); // 204
    }

    /**
     * 공개된 공부 기록 리스트 (검색 + 페이징)
     *
     * 예시 요청:
     * GET /api/posts/public?page=0&size=10&category=자바&keyword=JVM&tag=spring
     */
    @GetMapping("/public")
    public ResponseEntity<Page<PostResponse>> getPublicPosts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<PostResponse> page = postService.searchPublicPosts(category, keyword, tag, pageable);
        return ResponseEntity.ok(page);
    }



}