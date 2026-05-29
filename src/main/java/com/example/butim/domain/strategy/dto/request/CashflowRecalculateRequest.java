package com.example.butim.domain.strategy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CashflowRecalculateRequest(
        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId,

        List<Long> appliedItemIds,

        List<Long> receivedItemIds,

        @Min(value = 0, message = "병원비는 0 이상이어야 합니다.")
        Integer hospitalCost,

        @Min(value = 0, message = "보험금은 0 이상이어야 합니다.")
        Integer insuranceAmount,

        @Min(value = 0, message = "현재 자산은 0 이상이어야 합니다.")
        Integer currentAsset
) {
}