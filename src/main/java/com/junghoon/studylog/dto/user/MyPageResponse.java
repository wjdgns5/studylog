package com.junghoon.studylog.dto.user;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyPageResponse {

    private String email;
    private LocalDateTime joinedAt;

    private long totalPosts;
    private long publicPosts;
    private long privatePosts;
}