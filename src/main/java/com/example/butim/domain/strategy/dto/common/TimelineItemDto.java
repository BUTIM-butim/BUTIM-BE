package com.example.butim.domain.strategy.dto.common;

import com.example.butim.domain.strategy.enums.CashflowEventType;

import java.time.LocalDate;

public record TimelineItemDto(
        LocalDate date,
        Integer dayOffset,
        CashflowEventType eventType,
        String eventName,
        Integer amount,
        Integer balance
) {
}