package com.example.butim.domain.strategy.dto.common;

import com.example.butim.domain.strategy.enums.StrategyType;

import java.util.List;

public record StrategyCardDto(
        StrategyType strategyType,
        String title,
        String summary,
        Integer totalExpectedAmount,
        List<StrategyItemDto> supportItems,
        List<StrategyItemDto> loanItems
) {
}