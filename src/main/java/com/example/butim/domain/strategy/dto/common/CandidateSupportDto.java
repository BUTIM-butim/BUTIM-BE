package com.example.butim.domain.strategy.dto.common;

import com.example.butim.domain.strategy.enums.StrategyItemType;

public record CandidateSupportDto(
        String externalId,
        StrategyItemType itemType,
        String name,
        String description,
        Integer expectedAmount,
        Boolean repaymentRequired,
        Boolean overlapsWithWorkersCompensation,
        String applyUrl,
        Integer expectedReceiveDay,
        String source
) {
}