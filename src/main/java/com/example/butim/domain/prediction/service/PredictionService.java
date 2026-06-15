package com.example.butim.domain.prediction.service;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.accident.repository.AccidentInfoRepository;
import com.example.butim.domain.prediction.dto.response.PredictionResponse;
import com.example.butim.domain.prediction.entity.Prediction;
import com.example.butim.domain.prediction.entity.Reliability;
import com.example.butim.domain.prediction.repository.PredictionRepository;
import com.example.butim.domain.prediction.stats.*;
import com.example.butim.domain.user.entity.User;
import com.example.butim.domain.user.repository.UserRepository;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

    private final AccidentInfoRepository accidentInfoRepository;
    private final PredictionRepository predictionRepository;
    private final UserRepository userRepository;

    private final IndustryJobApprovalStatsService industryJobApprovalStatsService;
    private final DiagnosisStatsService diagnosisStatsService;
    private final CorrectionFactorService correctionFactorService;
    private final PrecedentStatsService precedentStatsService;
    private final ReliabilityCalculator reliabilityCalculator;
    private final PredictionGptService predictionGptService;

    @Transactional
    public PredictionResponse predict(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        AccidentInfo accidentInfo = accidentInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCIDENT_INFO_NOT_FOUND));

        // 통계 데이터 수집
        ApprovalStats approvalStats = industryJobApprovalStatsService.calculate(accidentInfo);

        String diagnosisCode = accidentInfo.getDiagnosisCode() != null
                ? accidentInfo.getDiagnosisCode().getCode() : null;
        DiagnosisStats diagnosisStats = diagnosisCode != null
                ? diagnosisStatsService.calculate(diagnosisCode)
                : new DiagnosisStats(0, 0, 0, 0);

        CorrectionFactor correctionFactor = correctionFactorService.calculate(accidentInfo);
        PrecedentStats precedentStats = precedentStatsService.calculate(accidentInfo);

        // 신뢰도 산출
        boolean hasDiagnosisCode = diagnosisCode != null;
        Reliability reliability = reliabilityCalculator.calculate(
                diagnosisStats, approvalStats, precedentStats, hasDiagnosisCode);
        String reliabilityDescription = reliabilityCalculator.buildDescription(
                reliability, precedentStats, hasDiagnosisCode);

        // GPT 예측
        PredictionGptService.GptPredictionResult gptResult = predictionGptService.predict(
                accidentInfo, diagnosisStats, approvalStats, correctionFactor, precedentStats);

        // 저장 또는 업데이트
        Prediction prediction = predictionRepository.findByUserId(userId)
                .orElse(null);

        if (prediction == null) {
            prediction = Prediction.builder()
                    .user(user)
                    .predictionMinDays(gptResult.minDays())
                    .predictionMaxDays(gptResult.maxDays())
                    .predictionMedianDays(gptResult.medianDays())
                    .reliability(reliability)
                    .reliabilityDescription(reliabilityDescription)
                    .similarCaseCount(precedentStats.totalCount())
                    .analysisText(gptResult.analysisText())
                    .build();
        } else {
            prediction.update(
                    gptResult.minDays(),
                    gptResult.maxDays(),
                    gptResult.medianDays(),
                    reliability,
                    reliabilityDescription,
                    precedentStats.totalCount(),
                    gptResult.analysisText()
            );
        }

        predictionRepository.save(prediction);
        log.info("예측 완료 - userId: {}, min: {}, max: {}, median: {}",
                userId, gptResult.minDays(), gptResult.maxDays(), gptResult.medianDays());

        return PredictionResponse.from(prediction);
    }

    @Transactional(readOnly = true)
    public PredictionResponse getMe(Long userId) {
        Prediction prediction = predictionRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.PREDICTION_NOT_FOUND));
        return PredictionResponse.from(prediction);
    }
}
