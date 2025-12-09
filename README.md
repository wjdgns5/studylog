# StudyLog 📚  
> 공부 · 독서 내용을 기록하고, 태그/카테고리별로 정리하는 개인 + 스터디용 블로그

---

## 1. 프로젝트 소개

**StudyLog**는 개발 공부, 알고리즘 문제 풀이, 독서 기록 등을  
**체계적으로 기록하고 관리하기 위한 개인 스터디 플랫폼**입니다.

단순히 글만 쓰는 블로그가 아니라,

- 카테고리 / 태그로 정리하고  
- 공개 / 비공개를 구분해서 관리하며  
- 내 공부 기록과 패턴을 “마이페이지”에서 한눈에 볼 수 있도록

설계한 **신입 백엔드 개발자 포트폴리오용 프로젝트**입니다.

---

## 2. 주요 기능 (MVP)

### 2-1. 인증 / 인가

- 회원가입 (`/api/auth/signup`)
- 로그인 (`/api/auth/login`)
- **Spring Security + JWT 기반 인증**
  - Access Token 발급 및 검증
  - 로그인 성공 시 **JWT를 LocalStorage에 저장**
  - 매 요청마다 `Authorization: Bearer {token}` 헤더로 인증
- 인증 실패/권한 없음에 대한 **커스텀 에러 응답**
  - `JwtFilter`, `JwtAuthenticationEntryPoint`
  - `GlobalExceptionHandler` + `ErrorResponse` DTO

### 2-2. 게시글 (공부/독서 기록) 관리

- 게시글 작성 / 조회 / 수정 / 삭제 (CRUD)
- 필드
  - 제목, 내용
  - 카테고리 (예: 자바, 스프링, 알고리즘…)
  - 태그 (쉼표 구분, 예: `spring,jwt,security`)
  - 공개 여부 (`isPrivate`: 공개 / 비공개)
- 공개 글 목록 (비로그인 포함 누구나 조회 가능)
- 내 글 목록 (마이페이지에서만 조회)
- **검색 기능**
  - 키워드(제목/내용)
  - 카테고리
  - 태그
- 페이징 처리 (`Page<T>` 기반)

### 2-3. 마이페이지

- 내 프로필 조회
  - 이메일
  - 가입일
  - 전체 작성 글 수
  - 공개 글 수 / 비공개 글 수
- 내가 작성한 글 목록
- 내가 북마크한 글 목록

### 2-4. 북마크

- 글 상세 화면에서 **북마크 추가**
- 마이페이지에서 북마크한 글 목록 확인

---

## 3. 기술 스택

### Backend

- Java 17
- Spring Boot 3.x
- Spring Web (MVC)
- Spring Security 6
- JWT (jjwt 또는 io.jsonwebtoken.*)
- Spring Data JPA
- H2 Database (개발용)
- MySQL (운영/확장 고려)
- Gradle 또는 Maven (빌드 도구)

### Frontend

- Thymeleaf (서버 렌더링 템플릿)
- Bootstrap 4
- Vanilla JavaScript + Fetch API

### 기타

- Lombok
- Validation (jakarta.validation)
- Jackson (JSON 직렬화/역직렬화)

---

## 4. 아키텍처 구조

레이어드 아키텍처 기반으로 설계했습니다.

```text
com.junghoon.studylog
 ├─ domain        # 엔티티, 리포지토리
 ├─ dto           # 요청/응답 DTO, ErrorResponse 등
 ├─ service       # 비즈니스 로직
 ├─ controller
 │   ├─ api       # REST API 컨트롤러 (@RestController)
 │   └─ view      # 화면용 컨트롤러 (Thymeleaf View 반환)
 ├─ global
 │   ├─ jwt       # TokenProvider, JwtFilter, JwtAuthenticationEntryPoint
 │   ├─ error     # GlobalExceptionHandler, 커스텀 예외(Exception400 등)
 │   └─ config    # SecurityConfig, WebMvc 설정 등
 └─ StudyLogApplication.java
