package com.example.butim.domain.strategy.entity;

import com.example.butim.domain.strategy.enums.CashflowEventType;
import com.example.butim.domain.strategy.enums.StrategyType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cashflow_snapshot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CashflowSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "strategy_result_id", nullable = false)
    private Long strategyResultId;

    @Enumerated(EnumType.STRING)
    @Column(name = "strategy_type")
    private StrategyType strategyType;

    @Column(name = "snapshot_date", nullable = false)
    private LocalDate snapshotDate;

    @Column(name = "day_offset", nullable = false)
    private Integer dayOffset;

    @Column(name = "income", nullable = false)
    private Integer income;

    @Column(name = "expense", nullable = false)
    private Integer expense;

    @Column(name = "balance", nullable = false)
    private Integer balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private CashflowEventType eventType;

    @Column(name = "event_memo")
    private String eventMemo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}