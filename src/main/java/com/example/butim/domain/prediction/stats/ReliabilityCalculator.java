package com.example.butim.domain.prediction.stats;

import com.example.butim.domain.prediction.entity.Reliability;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReliabilityCalculator {

    private static final int HIGH_CASE_THRESHOLD = 100;
    private static final int MEDIUM_CASE_THRESHOLD = 20;

    public Reliability calculate(DiagnosisStats diagnosisStats, ApprovalStats approvalStats,
                                 PrecedentStats precedentStats, boolean hasDiagnosisCode) {
        int score = 0;

        // 주상병코드 존재 여부 (2점)
        if (hasDiagnosisCode) score += 2;

        // 판례 건수 기반 (최대 3점)
        int precedentTotal = precedentStats.totalCount();
        if (precedentTotal >= HIGH_CASE_THRESHOLD) score += 3;
        else if (precedentTotal >= MEDIUM_CASE_THRESHOLD) score += 2;
        else if (precedentTotal > 0) score += 1;

        // 업종 승인율 데이터 존재 여부 (1점)
        if (approvalStats.industryApprovalRate() > 0) score += 1;

        // 직종 승인율 데이터 존재 여부 (1점)
        if (approvalStats.jobApprovalRate() > 0) score += 1;

        Reliability reliability = scoreToReliability(score);
        log.info("신뢰도 산출 - score: {}, result: {}", score, reliability);
        return reliability;
    }

    public String buildDescription(Reliability reliability, PrecedentStats precedentStats,
                                   boolean hasDiagnosisCode) {
        return switch (reliability) {
            case HIGH -> String.format(
                    "유사 판례 %d건(승인율 %.0f%%)과 업종·직종별 통계를 모두 활용하여 높은 신뢰도로 예측했습니다.",
                    precedentStats.totalCount(), precedentStats.approvalRate() * 100
            );
            case MEDIUM -> {
                if (!hasDiagnosisCode) {
                    yield "주상병코드가 입력되지 않아 판례 및 업종·직종 통계만으로 예측했습니다. 주상병코드를 입력하면 더 정확한 예측이 가능합니다.";
                }
                yield String.format(
                        "유사 판례 %d건을 활용했으나 데이터가 제한적입니다. 결과를 참고용으로 활용하세요.",
                        precedentStats.totalCount()
                );
            }
            case LOW -> "유사 판례 및 통계 데이터가 부족하여 예측 신뢰도가 낮습니다. 결과를 참고용으로만 활용하세요.";
        };
    }

    private Reliability scoreToReliability(int score) {
        if (score >= 5) return Reliability.HIGH;
        if (score >= 3) return Reliability.MEDIUM;
        return Reliability.LOW;
    }
}
