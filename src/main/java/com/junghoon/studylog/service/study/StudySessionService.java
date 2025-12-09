package com.junghoon.studylog.service.study;

import com.junghoon.studylog.domain.study.StudySession;
import com.junghoon.studylog.domain.user.User;
import com.junghoon.studylog.dto.study.DailyStudySummary;
import com.junghoon.studylog.dto.study.MonthlyStudySummary;
import com.junghoon.studylog.dto.study.StudySessionCreateRequest;
import com.junghoon.studylog.global.error.Exception400;
import com.junghoon.studylog.repository.study.StudySessionRepository;
import com.junghoon.studylog.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudySessionService {

    private final StudySessionRepository studySessionRepository;
    private final UserRepository userRepository;

    /**
     * 공부 기록 1개 저장
     */
    public void createSession(StudySessionCreateRequest request, String email) {

        if (request.getStudyDate() == null) {
            throw new Exception400("공부한 날짜는 필수입니다.");
        }
        if (request.getMinutes() <= 0) {
            throw new Exception400("공부 시간(분)은 1분 이상이어야 합니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        StudySession session = StudySession.builder()
                .studyDate(request.getStudyDate())
                .minutes(request.getMinutes())
                .category(request.getCategory())
                .memo(request.getMemo())
                .user(user)
                .build();

        studySessionRepository.save(session);
    }

    /**
     * 특정 기간 동안의 일별 공부 시간 합계
     */
    @Transactional(readOnly = true)
    public List<DailyStudySummary> getDailySummary(
            String email,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate == null || endDate == null) {
            throw new Exception400("startDate와 endDate는 필수입니다.");
        }
        if (endDate.isBefore(startDate)) {
            throw new Exception400("endDate는 startDate보다 이후여야 합니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        List<StudySession> sessions = studySessionRepository
                .findByUserAndStudyDateBetween(user, startDate, endDate);

        // 날짜별로 minutes 합계
        Map<LocalDate, Integer> grouped = new HashMap<>();
        for (StudySession session : sessions) {
            grouped.merge(
                    session.getStudyDate(),
                    session.getMinutes(),
                    Integer::sum
            );
        }

        // 날짜 오름차순 정렬 후 DTO로 변환
        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new DailyStudySummary(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 연도의 월별 공부 시간 합계
     */
    @Transactional(readOnly = true)
    public List<MonthlyStudySummary> getMonthlySummary(
            String email,
            int year
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception400("유저를 찾을 수 없습니다: " + email));

        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        List<StudySession> sessions =
                studySessionRepository.findByUserAndStudyDateBetween(user, start, end);

        // 월별로 minutes 합계
        Map<Integer, Integer> grouped = new HashMap<>(); // key: month(1~12)
        for (StudySession session : sessions) {
            int month = session.getStudyDate().getMonthValue();
            grouped.merge(month, session.getMinutes(), Integer::sum);
        }

        // 월 오름차순 정렬 후 DTO로 변환
        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new MonthlyStudySummary(year, e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}