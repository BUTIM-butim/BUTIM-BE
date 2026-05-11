package com.example.butim.domain.financial.dto.request;

import com.example.butim.domain.financial.enums.EmploymentStatus;
import com.example.butim.domain.financial.enums.HouseholdType;
import com.example.butim.domain.financial.enums.IncomeLevel;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FinancialInfoRequest {

    @NotNull(message = "산재 정보 ID는 필수입니다.")
    private Long accidentInfoId;

    @NotNull(message = "지역 ID는 필수입니다.")
    private Long regionId;

    @NotNull(message = "현재 자산은 필수입니다.")
    @Min(value = 0, message = "현재 자산은 0원 이상이어야 합니다.")
    private Integer currentAssets;

    @NotNull(message = "월 고정비는 필수입니다.")
    @Min(value = 0, message = "월 고정비는 0원 이상이어야 합니다.")
    private Integer monthlyFixedExpense;

    @NotNull(message = "기준 중위소득은 필수입니다.")
    private IncomeLevel incomeLevel;

    @NotNull(message = "복지 대상 유형은 필수입니다.")
    private HouseholdType householdType;

    @NotNull(message = "부양 가족 여부는 필수입니다.")
    private Boolean hasDependent;

    @NotNull(message = "자녀 여부는 필수입니다.")
    private Boolean hasChild;

    private Integer childCount;

    @NotNull(message = "임신 여부는 필수입니다.")
    private Boolean isPregnant;

    @NotNull(message = "장애 여부는 필수입니다.")
    private Boolean hasDisability;

    private Integer disabilityGrade;

    @NotNull(message = "현재 고용 상태는 필수입니다.")
    private EmploymentStatus currentEmploymentStatus;
}