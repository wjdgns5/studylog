package com.junghoon.studylog.config;

import com.junghoon.studylog.global.jwt.JwtAuthenticationEntryPoint;
import com.junghoon.studylog.global.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 X, JWT만 사용
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 401 처리
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // 로그인/회원가입은 모두 허용
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/posts/public/**").permitAll()
                        // 🔓 화면(HTML)용 URL들 – 토큰 없이도 들어올 수 있게
                        .requestMatchers(
                                "/",          // 홈
                                "/login",     // 로그인 페이지
                                "/signup",    // 회원가입 페이지
                                "/mypage",    // 마이페이지 화면 (안의 API 호출은 JWT 사용)
                                "/posts/**",  // 게시글 상세 화면 등
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/static/**"
                        ).permitAll()

                        // 공개 글 상세 조회는 누구나 볼 수 있게 허용
                        .requestMatchers(HttpMethod.GET, "/api/posts/*").permitAll()

                        .anyRequest().authenticated()                // 나머지는 인증 필요
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // H2 콘솔 사용 시 프레임 옵션 비활성화
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}