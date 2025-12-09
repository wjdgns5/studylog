package com.junghoon.studylog.controller.auth;

import com.junghoon.studylog.domain.user.User;
import com.junghoon.studylog.dto.LoginRequest;
import com.junghoon.studylog.dto.LoginResponse;
import com.junghoon.studylog.dto.SignupRequest;
import com.junghoon.studylog.global.error.Exception400;
import com.junghoon.studylog.global.jwt.TokenProvider;
import com.junghoon.studylog.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;        // SecurityConfig에서 BCryptPasswordEncoder 빈 등록했지?
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception400("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 로그인 -> JWT 발급
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // 1. email/password로 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. 인증 성공 시 JWT 생성
        String token = tokenProvider.createToken(authentication);

        // 3. 클라이언트에게 토큰 응답
        LoginResponse response = new LoginResponse(token);

        return ResponseEntity.ok(response);
    }



}
