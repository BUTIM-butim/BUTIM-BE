package com.example.butim.domain.strategy.dto.common;

import com.example.butim.domain.strategy.enums.CashflowEventType;
import com.example.butim.domain.strategy.enums.StrategyType;

import java.time.LocalDate;

public record CashflowPointDto(
        Long snapshotId,
        StrategyType strategyType,
        LocalDate snapshotDate,
        Integer dayOffset,
        Integer income,
        Integer expense,
        Integer balance,
        CashflowEventType eventType,
        String eventMemo
) {
}