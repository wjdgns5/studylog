package com.junghoon.studylog.domain.post;

import com.junghoon.studylog.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목
    @Column(nullable = false, length = 200)
    private String title;

    // 내용
    @Lob
    @Column(nullable = false)
    private String content;

    // 카테고리 (예: 자바, 스프링, 알고리즘)
    @Column(length = 50)
    private String category;

    // 태그 (처음엔 "java,spring" 이런 식의 문자열로만 관리해도 됨)
    @Column(length = 200)
    private String tags;

    // 공개/비공개
    @Column(nullable = false)
    private boolean isPrivate;

    // 작성자 (JWT에서 꺼낸 유저)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String title, String content, String category, String tags, boolean isPrivate) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.isPrivate = isPrivate;
    }
}