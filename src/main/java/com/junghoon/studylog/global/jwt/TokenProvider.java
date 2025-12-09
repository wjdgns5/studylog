package com.junghoon.studylog.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor // final로 선언된 필드(멤버 변수)들만 매개변수로 받는 생성자(constructor)
public class TokenProvider {

    private final UserDetailsService userDetailsService; // email(username)로 유저 정보 로드

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    private Key key;

    @PostConstruct //  객체(빈)의 생성 및 의존성 주입(DI)이 완료된 후에 실행되어야 하는 초기화 작업
    // 실행시점 : 생성자 호출 및 의존성 주입(@Autowired 등)이 모두 끝난 직후에 호출
    public void init() {
        // secretKey 문자열을 HMAC-SHA 키로 변환
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 인증 정보를 기반으로 Access Token 생성
     * @param authentication
     * @return
     */
    public String createToken(Authentication authentication) {

        String email = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityInSeconds * 1000);

        return Jwts.builder()
                .setSubject(email) // 토큰 주체 (Subject)
                .claim("auth", authorities) // 권한 정보
                .setIssuedAt(now) // 발급 시각
                .setExpiration(expiry) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /**
     * 토큰으로부터 Authentication 객체 생성
     * - SecurityContext에 넣어서 "로그인된 상태"로 만드는 핵심 메서드
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String email = claims.getSubject();
        if(email == null) {
            throw  new RuntimeException("토큰에 이메일 정보가 없습니다.");
        }

        // DB or 메모리에서 사용자 정보 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "", // 자격 증명(비밀번호)은 굳이 넣을 필요 없음
                userDetails.getAuthorities()
        );
    }

    /**
     * 토큰에서 Claims(내용) 꺼내기
     * @param token
     * @return
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰이어도 body는 꺼낼 수 있음
            return e.getClaims();
        }
    }

    /**
     * 토큰 유효성 검사
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // 파싱이 되면 정상

            return true;

        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다: {}", e.getMessage());
        } catch(UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다: {}", e.getMessage());
        }
        return  false;
    }


}
