package com.example.butim.domain.strategy.dto.request;

import jakarta.validation.constraints.NotNull;

public record StrategyRunRequest(
        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId,

        @NotNull(message = "산재정보 ID는 필수입니다.")
        Long accidentInfoId,

        @NotNull(message = "재정정보 ID는 필수입니다.")
        Long financialInfoId
) {
}
