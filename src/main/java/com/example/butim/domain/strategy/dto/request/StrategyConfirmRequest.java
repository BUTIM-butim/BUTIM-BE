package com.example.butim.domain.strategy.dto.request;

import com.example.butim.domain.strategy.enums.StrategyType;
import jakarta.validation.constraints.NotNull;

public record StrategyConfirmRequest(
        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId,

        @NotNull(message = "선택한 전략은 필수입니다.")
        StrategyType selectedStrategyType
) {
}