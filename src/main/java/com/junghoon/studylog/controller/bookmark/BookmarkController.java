package com.junghoon.studylog.controller.bookmark;

import com.junghoon.studylog.domain.post.PostResponse;
import com.junghoon.studylog.service.bookmark.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    /**
     * 게시글 북마크 추가
     * POST /api/bookmarks/{postId}
     */
    @PostMapping("/{postId}")
    public ResponseEntity<Void> addBookmark(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        bookmarkService.addBookmark(postId, email);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 북마크 해제
     * DELETE /api/bookmarks/{postId}
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> removeBookmark(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        bookmarkService.removeBookmark(postId, email);
        return ResponseEntity.noContent().build();
    }

    /**
     * 내가 북마크한 게시글 목록
     * GET /api/bookmarks/me
     */
    @GetMapping("/me")
    public ResponseEntity<List<PostResponse>> getMyBookmarks(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        List<PostResponse> bookmarks = bookmarkService.getMyBookmarks(email);
        return ResponseEntity.ok(bookmarks);
    }
}