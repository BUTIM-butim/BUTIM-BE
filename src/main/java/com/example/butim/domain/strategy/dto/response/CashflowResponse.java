package com.example.butim.domain.strategy.dto.response;

import com.example.butim.domain.strategy.dto.common.CashflowPointDto;
import com.example.butim.domain.strategy.dto.common.TimelineItemDto;

import java.util.List;

public record CashflowResponse(
        Long strategyResultId,
        Integer currentAsset,
        Integer cashGapDay,
        List<CashflowPointDto> cashflow,
        List<TimelineItemDto> timeline
) {
}