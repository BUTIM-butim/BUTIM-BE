package com.example.butim.domain.strategy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StrategyRunRequest(
        @NotNull(message = "사용자 ID는 필수입니다.")
        Long userId,

        Long accidentInfoId,

        Long financialInfoId,

        String regionCode,

        String regionName,

        String injuryName,

        String jobType,

        String employmentType,

        @NotNull(message = "현재 자산은 필수입니다.")
        @Min(value = 0, message = "현재 자산은 0 이상이어야 합니다.")
        Integer currentAsset,

        @NotNull(message = "월 생활비는 필수입니다.")
        @Min(value = 0, message = "월 생활비는 0 이상이어야 합니다.")
        Integer monthlyLivingCost,

        @Min(value = 0, message = "월 소득은 0 이상이어야 합니다.")
        Integer monthlyIncome,

        @Min(value = 1, message = "가구원 수는 1 이상이어야 합니다.")
        Integer householdSize,

        @NotNull(message = "현금 공백 발생일은 필수입니다.")
        @Min(value = 1, message = "현금 공백 발생일은 1 이상이어야 합니다.")
        Integer cashGapDay,

        @NotNull(message = "승인 예상 기간은 필수입니다.")
        @Min(value = 1, message = "승인 예상 기간은 1 이상이어야 합니다.")
        Integer approvalExpectedDays,

        @NotNull(message = "지급 예상 기간은 필수입니다.")
        @Min(value = 1, message = "지급 예상 기간은 1 이상이어야 합니다.")
        Integer paymentExpectedDays,

        @Min(value = 0, message = "산재 예상 수령액은 0 이상이어야 합니다.")
        Integer expectedWorkersCompensationAmount,

        @Min(value = 0, message = "병원비는 0 이상이어야 합니다.")
        Integer hospitalCost,

        @Min(value = 0, message = "보험금은 0 이상이어야 합니다.")
        Integer insuranceAmount
) {
}