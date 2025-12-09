package com.junghoon.studylog.controller.study;

import com.junghoon.studylog.dto.study.DailyStudySummary;
import com.junghoon.studylog.dto.study.MonthlyStudySummary;
import com.junghoon.studylog.dto.study.StudySessionCreateRequest;
import com.junghoon.studylog.service.study.StudySessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study-sessions")
public class StudySessionController {

    private final StudySessionService studySessionService;

    /**
     * 공부 기록 생성
     */
    @PostMapping
    public ResponseEntity<Void> createStudySession(
            @RequestBody StudySessionCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        studySessionService.createSession(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 일별 공부 시간 합계
     * 예: GET /api/study-sessions/daily?startDate=2025-12-01&endDate=2025-12-31
     */
    @GetMapping("/daily")
    public ResponseEntity<List<DailyStudySummary>> getDailySummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        String email = userDetails.getUsername();
        List<DailyStudySummary> result =
                studySessionService.getDailySummary(email, startDate, endDate);

        return ResponseEntity.ok(result);
    }

    /**
     * 월별 공부 시간 합계
     * 예: GET /api/study-sessions/monthly?year=2025
     */
    @GetMapping("/monthly")
    public ResponseEntity<List<MonthlyStudySummary>> getMonthlySummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int year
    ) {
        String email = userDetails.getUsername();
        List<MonthlyStudySummary> result =
                studySessionService.getMonthlySummary(email, year);

        return ResponseEntity.ok(result);
    }
}