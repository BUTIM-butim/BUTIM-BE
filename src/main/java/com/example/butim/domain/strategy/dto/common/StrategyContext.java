package com.example.butim.domain.strategy.dto.common;

public record StrategyContext(
        String regionCode,
        String regionName,
        String injuryName,
        String jobType,
        String employmentType,
        Integer currentAsset,
        Integer monthlyLivingCost,
        Integer cashGapDay,
        Integer approvalExpectedDays,
        Integer paymentExpectedDays
) {
}
