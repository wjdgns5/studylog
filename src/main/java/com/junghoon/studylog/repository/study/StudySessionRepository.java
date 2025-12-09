package com.junghoon.studylog.repository.study;

import com.junghoon.studylog.domain.study.StudySession;
import com.junghoon.studylog.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {

    // 특정 유저의 특정 기간 공부 기록
    List<StudySession> findByUserAndStudyDateBetween(
            User user,
            LocalDate startDate,
            LocalDate endDate
    );
}