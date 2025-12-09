package com.junghoon.studylog.dto.study;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class StudySessionCreateRequest {

    // 공부한 날짜 (yyyy-MM-dd 형식으로 들어올 거라고 가정)
    private LocalDate studyDate;

    // 공부 시간 (분)
    private int minutes;

    // 카테고리 (자바, 스프링, 알고리즘, 독서 등)
    private String category;

    // 메모 (선택)
    private String memo;
}