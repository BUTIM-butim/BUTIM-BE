package com.example.butim.domain.financial.dto.response;

import com.example.butim.domain.financial.entity.FinancialInfo;
import com.example.butim.domain.financial.enums.EmploymentStatus;
import com.example.butim.domain.financial.enums.HouseholdType;
import com.example.butim.domain.financial.enums.IncomeLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FinancialInfoResponse {

    private Long id;

    private Long userId;
    private Long accidentInfoId;

    private Long regionId;
    private String sidoCode;
    private String sidoName;
    private String sigunguCode;
    private String sigunguName;
    private String regionName;

    private Integer currentAssets;
    private Integer monthlyFixedExpense;

    private IncomeLevel incomeLevel;
    private HouseholdType householdType;

    private Boolean hasDependent;
    private Boolean hasChild;
    private Integer childCount;

    private Boolean isPregnant;
    private Boolean hasDisability;

    private EmploymentStatus currentEmploymentStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FinancialInfoResponse from(FinancialInfo financialInfo) {
        return FinancialInfoResponse.builder()
                .id(financialInfo.getId())
                .userId(financialInfo.getUser().getId())
                .accidentInfoId(financialInfo.getAccidentInfo().getId())

                .regionId(financialInfo.getRegion().getRegionId())
                .sidoCode(financialInfo.getRegion().getSidoCode())
                .sidoName(financialInfo.getRegion().getSidoName())
                .sigunguCode(financialInfo.getRegion().getSigunguCode())
                .sigunguName(financialInfo.getRegion().getSigunguName())
                .regionName(makeRegionName(financialInfo))

                .currentAssets(financialInfo.getCurrentAssets())
                .monthlyFixedExpense(financialInfo.getMonthlyFixedExpense())
                .incomeLevel(financialInfo.getIncomeLevel())
                .householdType(financialInfo.getHouseholdType())
                .hasDependent(financialInfo.getHasDependent())
                .hasChild(financialInfo.getHasChild())
                .childCount(financialInfo.getChildCount())
                .isPregnant(financialInfo.getIsPregnant())
                .hasDisability(financialInfo.getHasDisability())
                .currentEmploymentStatus(financialInfo.getCurrentEmploymentStatus())
                .createdAt(financialInfo.getCreatedAt())
                .updatedAt(financialInfo.getUpdatedAt())
                .build();
    }

    private static String makeRegionName(FinancialInfo financialInfo) {
        String sidoName = financialInfo.getRegion().getSidoName();
        String sigunguName = financialInfo.getRegion().getSigunguName();

        if (sigunguName == null || sigunguName.isBlank()) {
            return sidoName;
        }

        return sidoName + " " + sigunguName;
    }
}