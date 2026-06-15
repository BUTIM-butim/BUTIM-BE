package com.example.butim.domain.prediction.stats;

public record DiagnosisStats(
        int minDays,
        int maxDays,
        int medianDays,
        int similarCaseCount
) {
}
