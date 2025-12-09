package com.junghoon.studylog.domain.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    // 응답 DTO

    private final Long id;
    private final String title;
    private final String content;
    private final String category;
    private final String tags;
    private final boolean isPrivate;
    private final String authorEmail;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.tags = post.getTags();
        this.isPrivate = post.isPrivate();
        this.authorEmail = post.getAuthor().getEmail();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}