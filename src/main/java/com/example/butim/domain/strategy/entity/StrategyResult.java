package com.example.butim.domain.strategy.entity;

import com.example.butim.domain.strategy.enums.StrategyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "strategy_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StrategyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "accident_info_id")
    private Long accidentInfoId;

    @Column(name = "financial_info_id")
    private Long financialInfoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_strategy_type")
    private StrategyType selectedStrategyType;

    @Column(name = "current_asset", nullable = false)
    private Integer currentAsset;

    @Column(name = "monthly_living_cost", nullable = false)
    private Integer monthlyLivingCost;

    @Column(name = "cash_gap_day", nullable = false)
    private Integer cashGapDay;

    @Column(name = "approval_expected_days", nullable = false)
    private Integer approvalExpectedDays;

    @Column(name = "payment_expected_days", nullable = false)
    private Integer paymentExpectedDays;

    @Column(name = "expected_workers_compensation_amount")
    private Integer expectedWorkersCompensationAmount;

    @Column(name = "hospital_cost")
    private Integer hospitalCost;

    @Column(name = "insurance_amount")
    private Integer insuranceAmount;

    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void confirm(StrategyType strategyType) {
        this.selectedStrategyType = strategyType;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateCashflowInput(Integer currentAsset, Integer hospitalCost, Integer insuranceAmount) {
        if (currentAsset != null) {
            this.currentAsset = currentAsset;
        }
        if (hospitalCost != null) {
            this.hospitalCost = hospitalCost;
        }
        if (insuranceAmount != null) {
            this.insuranceAmount = insuranceAmount;
        }
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isConfirmed() {
        return selectedStrategyType != null;
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}