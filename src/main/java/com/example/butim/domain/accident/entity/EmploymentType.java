package com.example.butim.domain.accident.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmploymentType {
    REGULAR("정규직"),
    CONTRACT("계약직 (기간제 포함)"),
    DAILY("일용직 (하루 단위 근로)"),
    SPECIAL("특수형태근로종사자"),
    UNKNOWN("잘 모르겠어요");

    private final String label;
}
