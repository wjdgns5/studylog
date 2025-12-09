package com.junghoon.studylog.dto.study;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MonthlyStudySummary {

    private int year;
    private int month;        // 1 ~ 12
    private int totalMinutes;
}