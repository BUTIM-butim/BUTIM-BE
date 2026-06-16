package com.example.butim.domain.user.service;

import com.example.butim.domain.prediction.entity.Prediction;
import com.example.butim.domain.prediction.repository.PredictionRepository;
import com.example.butim.domain.strategy.entity.StrategyItem;
import com.example.butim.domain.strategy.entity.StrategyResult;
import com.example.butim.domain.strategy.enums.StrategyItemType;
import com.example.butim.domain.strategy.repository.StrategyItemRepository;
import com.example.butim.domain.strategy.repository.StrategyResultRepository;
import com.example.butim.domain.user.dto.response.UserMainResponse;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMainService {

    private final PredictionRepository predictionRepository;
    private final StrategyResultRepository strategyResultRepository;
    private final StrategyItemRepository strategyItemRepository;

    @Transactional(readOnly = true)
    public UserMainResponse getMain(Long userId) {
        Prediction prediction = predictionRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.PREDICTION_NOT_FOUND));

        StrategyResult strategyResult = strategyResultRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.STRATEGY_NOT_FOUND));

        List<StrategyItem> items = strategyItemRepository.findByStrategyResultIdOrderByIdAsc(strategyResult.getId());

        boolean hasSupportItems = items.stream()
                .anyMatch(item -> item.getItemType() != StrategyItemType.MICRO_FINANCE_LOAN);

        boolean hasLoanItems = items.stream()
                .anyMatch(item -> item.getItemType() == StrategyItemType.MICRO_FINANCE_LOAN);

        return new UserMainResponse(
                prediction.getPredictionMinDays(),
                prediction.getPredictionMaxDays(),
                prediction.getPredictionMedianDays(),
                prediction.getPredictionMedianDays() + 14,
                strategyResult.getPaymentExpectedDays(),
                hasSupportItems,
                hasLoanItems
        );
    }
}