package com.junghoon.studylog.controller.user;

import com.junghoon.studylog.dto.user.MyPageResponse;
import com.junghoon.studylog.service.user.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final MyPageService myPageService;

    @GetMapping("/me")
    public String me(@AuthenticationPrincipal UserDetails user) {

        return "현재 로그인한 사용자 이메일: " + user.getUsername();
    }

    /**
            * 마이페이지 정보 조회
     */
    @GetMapping("/me/profile")
    public ResponseEntity<MyPageResponse> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        MyPageResponse response = myPageService.getMyPage(email);
        return ResponseEntity.ok(response);
    }



}