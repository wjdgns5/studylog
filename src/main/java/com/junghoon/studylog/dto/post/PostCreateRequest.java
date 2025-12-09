package com.junghoon.studylog.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {
    // 글 작성 요청 DTO

    private String title;
    private String content;
    private String category;
    private String tags;
    private boolean isPrivate;
}
