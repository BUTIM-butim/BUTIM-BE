package com.example.butim.domain.strategy.entity;

import com.example.butim.domain.strategy.enums.StrategyItemType;
import com.example.butim.domain.strategy.enums.StrategyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "strategy_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StrategyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "strategy_result_id", nullable = false)
    private Long strategyResultId;

    @Enumerated(EnumType.STRING)
    @Column(name = "strategy_type", nullable = false)
    private StrategyType strategyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private StrategyItemType itemType;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_description", columnDefinition = "TEXT")
    private String itemDescription;

    @Column(name = "expected_amount")
    private Integer expectedAmount;

    @Column(name = "repayment_required")
    private Boolean repaymentRequired;

    @Column(name = "overlaps_with_workers_compensation")
    private Boolean overlapsWithWorkersCompensation;

    @Column(name = "apply_url")
    private String applyUrl;

    @Column(name = "expected_apply_date")
    private LocalDateTime expectedApplyDate;

    @Column(name = "expected_receive_date")
    private LocalDateTime expectedReceiveDate;

    @Column(name = "ai_reason", columnDefinition = "TEXT")
    private String aiReason;
}