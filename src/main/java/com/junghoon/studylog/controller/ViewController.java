package com.junghoon.studylog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        // 기본 화면: 공개 글 목록
        return "posts";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/mypage")
    public String myPage() {
        return "mypage";
    }

    @GetMapping("/posts/{id}")
    public String postDetail() {
        // 상세 화면 템플릿 이름
        return "post-detail";
    }

    @GetMapping("/posts/new")
    public String newPostPage() {
        return "new-post";
    }
}