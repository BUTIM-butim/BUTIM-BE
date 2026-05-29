package com.example.butim.domain.strategy.repository;

import com.example.butim.domain.strategy.entity.StrategyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StrategyResultRepository extends JpaRepository<StrategyResult, Long> {

    Optional<StrategyResult> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}