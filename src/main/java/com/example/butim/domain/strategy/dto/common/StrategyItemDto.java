package com.example.butim.domain.strategy.dto.common;

import com.example.butim.domain.strategy.enums.StrategyItemType;

import java.time.LocalDateTime;

public record StrategyItemDto(
        Long itemId,
        StrategyItemType itemType,
        String itemName,
        String itemDescription,
        Integer expectedAmount,
        Boolean repaymentRequired,
        Boolean overlapsWithWorkersCompensation,
        String applyUrl,
        LocalDateTime expectedApplyDate,
        LocalDateTime expectedReceiveDate,
        String aiReason
) {
}