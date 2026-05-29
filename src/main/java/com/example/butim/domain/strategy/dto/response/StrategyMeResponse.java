package com.example.butim.domain.strategy.dto.response;

import com.example.butim.domain.strategy.dto.common.CashflowPointDto;
import com.example.butim.domain.strategy.dto.common.StrategyCardDto;
import com.example.butim.domain.strategy.dto.common.TimelineItemDto;
import com.example.butim.domain.strategy.enums.StrategyType;

import java.util.List;

public record StrategyMeResponse(
        Long strategyResultId,
        Boolean confirmed,
        StrategyType selectedStrategyType,
        Integer currentAsset,
        Integer cashGapDay,
        Integer approvalExpectedDays,
        Integer paymentExpectedDays,
        List<StrategyCardDto> strategies,
        StrategyCardDto selectedStrategy,
        List<CashflowPointDto> cashflow,
        List<TimelineItemDto> timeline
) {
}