package com.junghoon.studylog.repository.bookmark;

import com.junghoon.studylog.domain.bookmark.Bookmark;
import com.junghoon.studylog.domain.post.Post;
import com.junghoon.studylog.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<Bookmark> findByUserAndPost(User user, Post post);

    List<Bookmark> findByUser(User user);

    long countByPost(Post post); // (선택) 게시글 북마크 수
}