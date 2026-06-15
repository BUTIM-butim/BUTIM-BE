package com.example.butim.domain.prediction.service;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.accident.repository.AccidentInfoRepository;
import com.example.butim.domain.prediction.csv.IndustryApprovalCsvReader;
import com.example.butim.domain.prediction.dto.response.PredictionResponse;
import com.example.butim.domain.prediction.entity.Prediction;
import com.example.butim.domain.prediction.entity.Reliability;
import com.example.butim.domain.prediction.repository.PredictionRepository;
import com.example.butim.domain.prediction.stats.DiagnosisStats;
import com.example.butim.domain.prediction.stats.DiagnosisStatsService;
import com.example.butim.domain.user.entity.User;
import com.example.butim.domain.user.repository.UserRepository;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final IndustryApprovalCsvReader industryApprovalCsvReader;
    private final DiagnosisStatsService diagnosisStatsService;
    private final PredictionGptService predictionGptService;
    private final ObjectMapper objectMapper;

    @Transactional
    public PredictionResponse predict(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        AccidentInfo accidentInfo = accidentInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCIDENT_INFO_NOT_FOUND));

        // CSV 기반 데이터 수집
        double industryApprovalRate = industryApprovalCsvReader
                .getApprovalRate(accidentInfo.getIndustry().getIndustryName());

        String diagnosisCode = accidentInfo.getDiagnosisCode() != null
                ? accidentInfo.getDiagnosisCode().getCode() : null;
        DiagnosisStats diagnosisStats = diagnosisCode != null
                ? diagnosisStatsService.calculate(diagnosisCode)
                : new DiagnosisStats(0, 0, 0, 0);

        // GPT 예측
        PredictionGptService.GptPredictionResult gptResult = predictionGptService.predict(
                accidentInfo, industryApprovalRate, diagnosisStats);

        Reliability reliability = parseReliability(gptResult.reliabilityLevel());
        String analysisText = serializeReasons(gptResult.analysisReasons());

        // 저장 또는 업데이트
        Prediction prediction = predictionRepository.findByUserId(userId).orElse(null);

        if (prediction == null) {
            prediction = Prediction.builder()
                    .user(user)
                    .predictionMinDays(gptResult.minDays())
                    .predictionMaxDays(gptResult.maxDays())
                    .predictionMedianDays(gptResult.medianDays())
                    .reliability(reliability)
                    .reliabilityScore(gptResult.reliabilityScore())
                    .similarCaseCount(gptResult.similarCaseCount())
                    .analysisText(analysisText)
                    .build();
        } else {
            prediction.update(
                    gptResult.minDays(),
                    gptResult.maxDays(),
                    gptResult.medianDays(),
                    reliability,
                    gptResult.reliabilityScore(),
                    gptResult.similarCaseCount(),
                    analysisText
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

    private Reliability parseReliability(String level) {
        return switch (level) {
            case "HIGH" -> Reliability.HIGH;
            case "LOW" -> Reliability.LOW;
            default -> Reliability.MEDIUM;
        };
    }

    private String serializeReasons(java.util.List<String> reasons) {
        try {
            return objectMapper.writeValueAsString(reasons);
        } catch (Exception e) {
            log.warn("analysisReasons 직렬화 실패", e);
            return "[]";
        }
    }
}
