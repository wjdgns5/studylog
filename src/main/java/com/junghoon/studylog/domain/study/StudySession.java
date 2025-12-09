package com.junghoon.studylog.domain.study;

import com.junghoon.studylog.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 공부한 날짜 (하루 단위 집계용)
    @Column(nullable = false)
    private LocalDate studyDate;

    // 공부 시간 (분 단위)
    @Column(nullable = false)
    private int minutes;

    // 어떤 공부인지 (자바, 스프링, 알고리즘, 독서 등)
    @Column(length = 50)
    private String category;

    // 간단 메모
    @Column(length = 255)
    private String memo;

    // 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}