package com.example.butim.domain.strategy.repository;

import com.example.butim.domain.strategy.entity.CashflowSnapshot;
import com.example.butim.domain.strategy.enums.StrategyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CashflowSnapshotRepository extends JpaRepository<CashflowSnapshot, Long> {

    List<CashflowSnapshot> findByStrategyResultIdOrderByDayOffsetAscIdAsc(Long strategyResultId);

    List<CashflowSnapshot> findByStrategyResultIdAndStrategyTypeOrderByDayOffsetAscIdAsc(
            Long strategyResultId,
            StrategyType strategyType
    );

    void deleteByStrategyResultIdAndStrategyType(Long strategyResultId, StrategyType strategyType);
}