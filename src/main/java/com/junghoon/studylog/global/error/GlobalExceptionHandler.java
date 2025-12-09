package com.junghoon.studylog.global.error;

import com.junghoon.studylog.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.List.of;

@ControllerAdvice // 예외를 '전역적으로(Globally)' 처리하기 위한 어노테이션
public class GlobalExceptionHandler{

    /**
     * 실제 HTTP 응답 형식

     HTTP/1.1 400 Bad Request        ← ✅ 상태줄(Status line)
     Content-Type: application/json  ← ✅ 헤더(Header)
     Content-Length: 123             ← ✅ 헤더(Header)

     {                               ← ✅ 바디(Body)
        "status": 400,
        "message": "제목은 필수입니다.",
        "path": "/api/posts",
        "timestamp": "2025-12-09T21:40:00.123"
     }
     */

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<ErrorResponse> handleException400 (Exception400 e, HttpServletRequest request) {
        // ErrorResponse : 오류 발생 시 클라이언트에게 반환되는 응답 데이터의 구조를 표준화하고 정의하는 역할

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(), // 응답값 400
                e.getMessage(), // 메세지
                request.getRequestURI() // path 경로
        );
            // ResponseEntity.status() : 스프링(Spring) 프레임워크에서 HTTP 응답을 직접 설정하는 메서드
            // .status(HttpStatus.BAD_REQUEST) : 400 + Bad Request
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<ErrorResponse> handleException401 (Exception401 e, HttpServletRequest request) {

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(body);
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<ErrorResponse> handleException403(Exception403 e, HttpServletRequest request) {

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.FORBIDDEN.value(), // 오류 숫자
                e.getMessage(), // 오류 메세지
                request.getRequestURI() // 오류 경로
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(body);
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<ErrorResponse> handleException404(Exception404 e, HttpServletRequest request) {

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(body);
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<ErrorResponse> handleException500(Exception500 e, HttpServletRequest request) {

        ErrorResponse body = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }

}
