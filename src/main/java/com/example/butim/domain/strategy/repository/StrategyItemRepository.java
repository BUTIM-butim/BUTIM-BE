package com.example.butim.domain.strategy.repository;

import com.example.butim.domain.strategy.entity.StrategyItem;
import com.example.butim.domain.strategy.enums.StrategyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StrategyItemRepository extends JpaRepository<StrategyItem, Long> {

    List<StrategyItem> findByStrategyResultIdOrderByIdAsc(Long strategyResultId);

    List<StrategyItem> findByStrategyResultIdAndStrategyTypeOrderByIdAsc(
            Long strategyResultId,
            StrategyType strategyType
    );
}