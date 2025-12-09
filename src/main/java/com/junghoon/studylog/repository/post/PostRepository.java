package com.junghoon.studylog.repository.post;

import com.junghoon.studylog.domain.post.Post;
import com.junghoon.studylog.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 내 글 목록 조회
    List<Post> findByAuthor(User author);

    // 전체 글 개수
    long countByAuthor(User author);

    // 공개 글 개수
    long countByAuthorAndIsPrivateFalse(User author);

    // 비공개 글 개수
    long countByAuthorAndIsPrivateTrue(User author);


    /**
     * 공개 글들(public) 중에서
     * - 카테고리(옵션)
     * - 태그 포함 여부(옵션)
     * - 키워드(제목/내용에 포함, 옵션)
     * 으로 검색 + 페이징
     */
    @Query("""
        SELECT p FROM Post p
        WHERE p.isPrivate = false
          AND (:category IS NULL OR p.category = :category)
          AND (:keyword IS NULL OR (p.title LIKE %:keyword% OR p.content LIKE %:keyword%))
          AND (:tag IS NULL OR p.tags LIKE %:tag%)
        """)
    Page<Post> searchPublicPosts(
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("tag") String tag,
            Pageable pageable
    );

    /**
     * (:category IS NULL OR p.category = :category) → 파라미터가 null이면 조건 생략
     * LIKE %:keyword% → 제목/내용에 키워드가 포함되면 검색
     * Page<Post> + Pageable → 페이징 결과
     */

}
