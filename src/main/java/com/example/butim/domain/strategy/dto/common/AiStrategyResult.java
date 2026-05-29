package com.example.butim.domain.strategy.dto.common;

import java.util.List;

public record AiStrategyResult(
        String summary,
        List<AiStrategyPlan> strategies
) {
}