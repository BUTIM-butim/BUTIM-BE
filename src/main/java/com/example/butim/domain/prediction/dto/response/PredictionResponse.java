package com.example.butim.domain.prediction.dto.response;

import com.example.butim.domain.prediction.entity.Prediction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class PredictionResponse {

    private Long id;
    private Integer predictionMinDays;
    private Integer predictionMaxDays;
    private Integer predictionMedianDays;
    private Integer paymentDays;
    private String reliability;
    private Integer reliabilityScore;
    private Integer similarCaseCount;
    private List<String> analysisReasons;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static PredictionResponse from(Prediction prediction) {
        return PredictionResponse.builder()
                .id(prediction.getId())
                .predictionMinDays(prediction.getPredictionMinDays())
                .predictionMaxDays(prediction.getPredictionMaxDays())
                .predictionMedianDays(prediction.getPredictionMedianDays())
                .paymentDays(prediction.getPredictionMedianDays() + 14)
                .reliability(prediction.getReliability().getLabel())
                .reliabilityScore(prediction.getReliabilityScore())
                .similarCaseCount(prediction.getSimilarCaseCount())
                .analysisReasons(deserializeReasons(prediction.getAnalysisText()))
                .createdAt(prediction.getCreatedAt())
                .updatedAt(prediction.getUpdatedAt())
                .build();
    }

    private static List<String> deserializeReasons(String json) {
        try {
            return MAPPER.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
