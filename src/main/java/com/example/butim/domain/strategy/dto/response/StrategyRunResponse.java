package com.example.butim.domain.strategy.dto.response;

import com.example.butim.domain.strategy.dto.common.CashflowPointDto;
import com.example.butim.domain.strategy.dto.common.StrategyCardDto;

import java.util.List;

public record StrategyRunResponse(
        Long strategyResultId,
        Integer currentAsset,
        Integer cashGapDay,
        Integer approvalExpectedDays,
        Integer paymentExpectedDays,
        List<StrategyCardDto> strategies,
        List<CashflowPointDto> cashflow
) {
}