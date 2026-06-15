package com.example.butim.domain.prediction.entity;

import com.example.butim.domain.user.entity.User;
import com.example.butim.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "prediction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Prediction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "prediction_min_days", nullable = false)
    private Integer predictionMinDays;

    @Column(name = "prediction_max_days", nullable = false)
    private Integer predictionMaxDays;

    @Column(name = "prediction_median_days", nullable = false)
    private Integer predictionMedianDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Reliability reliability;

    @Column(name = "reliability_description", nullable = false)
    private String reliabilityDescription;

    @Column(name = "similar_case_count", nullable = false)
    private Integer similarCaseCount;

    @Column(name = "analysis_text", nullable = false, columnDefinition = "TEXT")
    private String analysisText;

    @Builder
    public Prediction(User user, Integer predictionMinDays, Integer predictionMaxDays,
                      Integer predictionMedianDays, Reliability reliability,
                      String reliabilityDescription, Integer similarCaseCount, String analysisText) {
        this.user = user;
        this.predictionMinDays = predictionMinDays;
        this.predictionMaxDays = predictionMaxDays;
        this.predictionMedianDays = predictionMedianDays;
        this.reliability = reliability;
        this.reliabilityDescription = reliabilityDescription;
        this.similarCaseCount = similarCaseCount;
        this.analysisText = analysisText;
    }

    public void update(Integer predictionMinDays, Integer predictionMaxDays,
                       Integer predictionMedianDays, Reliability reliability,
                       String reliabilityDescription, Integer similarCaseCount, String analysisText) {
        this.predictionMinDays = predictionMinDays;
        this.predictionMaxDays = predictionMaxDays;
        this.predictionMedianDays = predictionMedianDays;
        this.reliability = reliability;
        this.reliabilityDescription = reliabilityDescription;
        this.similarCaseCount = similarCaseCount;
        this.analysisText = analysisText;
    }
}
