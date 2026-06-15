package com.example.butim.domain.prediction.dto.response;

import com.example.butim.domain.prediction.entity.Prediction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PredictionResponse {

    private Long id;
    private Integer predictionMinDays;
    private Integer predictionMaxDays;
    private Integer predictionMedianDays;
    private String reliability;
    private String reliabilityDescription;
    private Integer similarCaseCount;
    private String analysisText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PredictionResponse from(Prediction prediction) {
        return PredictionResponse.builder()
                .id(prediction.getId())
                .predictionMinDays(prediction.getPredictionMinDays())
                .predictionMaxDays(prediction.getPredictionMaxDays())
                .predictionMedianDays(prediction.getPredictionMedianDays())
                .reliability(prediction.getReliability().getLabel())
                .reliabilityDescription(prediction.getReliabilityDescription())
                .similarCaseCount(prediction.getSimilarCaseCount())
                .analysisText(prediction.getAnalysisText())
                .createdAt(prediction.getCreatedAt())
                .updatedAt(prediction.getUpdatedAt())
                .build();
    }
}
