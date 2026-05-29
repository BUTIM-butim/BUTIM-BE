package com.example.butim.domain.strategy.dto.common;

import java.util.List;

public record AiStrategyPlan(
        String strategyType,
        String title,
        String summary,
        List<AiStrategyItem> items
) {
    public record AiStrategyItem(
            String externalId,
            String itemName,
            String itemType,
            Integer expectedAmount,
            Boolean repaymentRequired,
            Boolean overlapsWithWorkersCompensation,
            Integer expectedReceiveDay,
            String aiReason
    ) {
    }
}