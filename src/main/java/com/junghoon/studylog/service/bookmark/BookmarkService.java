package com.junghoon.studylog.service.bookmark;

import com.junghoon.studylog.domain.bookmark.Bookmark;
import com.junghoon.studylog.domain.post.Post;
import com.junghoon.studylog.domain.post.PostResponse;
import com.junghoon.studylog.domain.user.User;
import com.junghoon.studylog.global.error.Exception400;
import com.junghoon.studylog.global.error.Exception404;
import com.junghoon.studylog.repository.bookmark.BookmarkRepository;
import com.junghoon.studylog.repository.post.PostRepository;
import com.junghoon.studylog.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 게시글 북마크 추가
     */
    public void addBookmark(Long postId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다. id=" + postId));

        // 이미 북마크 되어 있으면 그냥 무시 (idempotent)
        if (bookmarkRepository.existsByUserAndPost(user, post)) {
            return;
        }

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .post(post)
                .build();

        bookmarkRepository.save(bookmark);
    }

    /**
     * 게시글 북마크 해제
     */
    public void removeBookmark(Long postId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다. id=" + postId));

        bookmarkRepository.findByUserAndPost(user, post)
                .ifPresent(bookmarkRepository::delete);
        // 없으면 아무 것도 안 함 (idempotent)
    }

    /**
     * 내가 북마크한 게시글 목록
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getMyBookmarks(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        List<Bookmark> bookmarks = bookmarkRepository.findByUser(user);

        return bookmarks.stream()
                .map(Bookmark::getPost)
                .map(PostResponse::new)
                .toList();
    }
}