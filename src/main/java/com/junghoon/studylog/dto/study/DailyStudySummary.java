package com.junghoon.studylog.dto.study;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyStudySummary {

    private LocalDate date;
    private int totalMinutes;
}