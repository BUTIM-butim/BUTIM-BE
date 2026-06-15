package com.example.butim.domain.prediction.repository;

import com.example.butim.domain.prediction.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    boolean existsByUserId(Long userId);

    Optional<Prediction> findByUserId(Long userId);
}
