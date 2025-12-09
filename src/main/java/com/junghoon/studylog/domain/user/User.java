package com.junghoon.studylog.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인에 사용할 이메일
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // BCrypt로 암호화된 비밀번호
    @Column(nullable = false)
    private String password;

    // 권한 (예: ROLE_USER, ROLE_ADMIN)
    @Column(nullable = false, length = 50)
    private String role;

    private LocalDateTime createdAt;

    @PrePersist //  엔티티가 데이터베이스에 저장(persist)되기 직전에 특정 메서드를 실행하도록 지정
    // 엔티티의 createdAt (생성일시) 또는 createdBy (생성자) 필드를 자동으로 설정하는 것
    // 애플리케이션 레벨에서 이 값을 설정하면 비즈니스 로직 전반에서 누락되거나 실수로 잘못된 값이 입력되는 것을 방지
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if(this.role == null) {
            this.role = "ROLE_USER";
        }
    }

}