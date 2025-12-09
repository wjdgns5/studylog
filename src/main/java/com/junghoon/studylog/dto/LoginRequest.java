package com.junghoon.studylog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    // 로그인/회원가입 DTO

    private String email;
    private String password;
}