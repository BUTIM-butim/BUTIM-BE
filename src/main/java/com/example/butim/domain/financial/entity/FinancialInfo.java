package com.example.butim.domain.financial.entity;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.financial.enums.EmploymentStatus;
import com.example.butim.domain.financial.enums.HouseholdType;
import com.example.butim.domain.financial.enums.IncomeLevel;
import com.example.butim.domain.region.entity.Region;
import com.example.butim.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "financial_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FinancialInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 산재 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accident_info_id", nullable = false)
    private AccidentInfo accidentInfo;

    // 지역
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    // 현재 자산
    @Column(name = "current_assets", nullable = false)
    private Integer currentAssets;

    // 월 고정비
    @Column(name = "monthly_fixed_expense", nullable = false)
    private Integer monthlyFixedExpense;

    // 기준 중위소득
    @Enumerated(EnumType.STRING)
    @Column(name = "income_level", nullable = false)
    private IncomeLevel incomeLevel;

    // 복지 대상 유형
    @Enumerated(EnumType.STRING)
    @Column(name = "household_type", nullable = false)
    private HouseholdType householdType;

    // 부양 가족 여부
    @Column(name = "has_dependent", nullable = false)
    private Boolean hasDependent;

    // 자녀 여부
    @Column(name = "has_child", nullable = false)
    private Boolean hasChild;

    // 자녀 수
    @Column(name = "child_count")
    private Integer childCount;

    // 임신 여부
    @Column(name = "is_pregnant", nullable = false)
    private Boolean isPregnant;

    // 장애 여부
    @Column(name = "has_disability", nullable = false)
    private Boolean hasDisability;

    // 장애 등급
    @Column(name = "disability_grade")
    private Integer disabilityGrade;

    // 현재 고용 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "current_employment_status", nullable = false)
    private EmploymentStatus currentEmploymentStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void update(
            Region region,
            Integer currentAssets,
            Integer monthlyFixedExpense,
            IncomeLevel incomeLevel,
            HouseholdType householdType,
            Boolean hasDependent,
            Boolean hasChild,
            Integer childCount,
            Boolean isPregnant,
            Boolean hasDisability,
            Integer disabilityGrade,
            EmploymentStatus currentEmploymentStatus
    ) {
        this.region = region;
        this.currentAssets = currentAssets;
        this.monthlyFixedExpense = monthlyFixedExpense;
        this.incomeLevel = incomeLevel;
        this.householdType = householdType;
        this.hasDependent = hasDependent;
        this.hasChild = hasChild;
        this.childCount = childCount;
        this.isPregnant = isPregnant;
        this.hasDisability = hasDisability;
        this.disabilityGrade = disabilityGrade;
        this.currentEmploymentStatus = currentEmploymentStatus;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.childCount == null) {
            this.childCount = 0;
        }

        if (this.disabilityGrade == null) {
            this.disabilityGrade = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();

        if (this.childCount == null) {
            this.childCount = 0;
        }

        if (this.disabilityGrade == null) {
            this.disabilityGrade = 0;
        }
    }
}