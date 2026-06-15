package com.example.butim.domain.prediction.stats;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.accident.entity.BusinessSize;
import com.example.butim.domain.prediction.client.PredictionDataClient;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CorrectionFactorService {

    private final PredictionDataClient predictionDataClient;

    public CorrectionFactor calculate(AccidentInfo accidentInfo) {
        double businessSizeMultiplier = calculateBusinessSizeMultiplier(accidentInfo.getBusinessSize());
        double ageMultiplier = calculateAgeMultiplier(accidentInfo.getAge());

        log.info("보정계수 계산 완료 - 사업장규모: {}, 나이: {}", businessSizeMultiplier, ageMultiplier);
        return new CorrectionFactor(businessSizeMultiplier, ageMultiplier);
    }

    private double calculateBusinessSizeMultiplier(BusinessSize businessSize) {
        JsonNode applyData = predictionDataClient.fetchBusinessSizeApplyData();
        JsonNode approvalData = predictionDataClient.fetchBusinessSizeApprovalData();

        String targetLabel = mapBusinessSizeToApiLabel(businessSize);

        double overallRate = calculateOverallRate(applyData, approvalData);
        double targetRate = calculateRateForCategory(applyData, approvalData, targetLabel, BUSINESS_SIZE_FIELDS);

        if (overallRate == 0 || targetRate == 0) return 1.0;

        // 전체 평균 대비 해당 규모의 승인율 비율 (높을수록 유리 → 계수 낮춤)
        return overallRate / targetRate;
    }

    private double calculateAgeMultiplier(int age) {
        JsonNode applyData = predictionDataClient.fetchAgeApplyData();
        JsonNode approvalData = predictionDataClient.fetchAgeApprovalData();

        String ageGroup = mapAgeToApiLabel(age);

        double overallRate = calculateOverallRate(applyData, approvalData);
        double targetRate = calculateRateForCategory(applyData, approvalData, ageGroup, AGE_FIELDS);

        if (overallRate == 0 || targetRate == 0) return 1.0;

        return overallRate / targetRate;
    }

    // TODO: OPA001MT_11_INFO 응답 확인 후 분류명 수정
    private String mapBusinessSizeToApiLabel(BusinessSize businessSize) {
        return switch (businessSize) {
            case UNDER_5 -> "5인미만";
            case FROM_5_TO_9 -> "5~9인";
            case FROM_10_TO_29 -> "10~29인";
            case FROM_30_TO_49 -> "30~49인";
            case FROM_50_TO_99 -> "50~99인";
            case FROM_100_TO_299 -> "100~299인";
            case FROM_300_TO_499 -> "300~499인";
            case FROM_500_TO_999 -> "500~999인";
            case OVER_1000 -> "1000인이상";
        };
    }

    // TODO: 실제 API 응답 확인 후 나이 그룹 분류명 수정
    private String mapAgeToApiLabel(int age) {
        if (age < 20) return "20세미만";
        if (age < 30) return "20~29세";
        if (age < 40) return "30~39세";
        if (age < 50) return "40~49세";
        if (age < 60) return "50~59세";
        if (age < 70) return "60~69세";
        return "70세이상";
    }

    private double calculateOverallRate(JsonNode applyData, JsonNode approvalData) {
        long totalApply = sumAllCounts(applyData, APPLY_COUNT_FIELDS);
        long totalApproval = sumAllCounts(approvalData, APPROVAL_COUNT_FIELDS);
        if (totalApply == 0) return 0.0;
        return (double) totalApproval / totalApply;
    }

    private double calculateRateForCategory(JsonNode applyData, JsonNode approvalData,
                                            String targetLabel, String[] nameFields) {
        long applyCount = sumCountForLabel(applyData, targetLabel, nameFields, APPLY_COUNT_FIELDS);
        long approvalCount = sumCountForLabel(approvalData, targetLabel, nameFields, APPROVAL_COUNT_FIELDS);
        if (applyCount == 0) return 0.0;
        return (double) approvalCount / applyCount;
    }

    private long sumAllCounts(JsonNode root, String[] countFields) {
        return extractItems(root).stream()
                .mapToLong(item -> extractCount(item, countFields))
                .sum();
    }

    private long sumCountForLabel(JsonNode root, String targetLabel, String[] nameFields, String[] countFields) {
        long total = 0;
        for (JsonNode item : extractItems(root)) {
            String itemLabel = extractText(item, nameFields);
            if (itemLabel != null && normalize(itemLabel).contains(normalize(targetLabel))) {
                total += extractCount(item, countFields);
            }
        }
        return total;
    }

    // TODO: OPA001MT_11_INFO / OPA001MT_21_INFO 응답 확인 후 수정
    private static final String[] BUSINESS_SIZE_FIELDS = {
            "bplc_sz_cd", "bplc_sz_nm"
    };

    // TODO: OPA252MT_11_INFO / OPA252MT_21_INFO 응답 확인 후 수정
    private static final String[] AGE_FIELDS = {
            "age_grop_cd", "age_grop_nm"
    };

    private static final String[] APPLY_COUNT_FIELDS = {
            "ia_rcpr_aply_nocs"
    };

    // TODO: 승인 데이터 응답 확인 후 수정
    private static final String[] APPROVAL_COUNT_FIELDS = {
            "ia_grnt_nocs", "ia_aprvl_nocs", "ia_rcpr_aply_nocs"
    };

    private long extractCount(JsonNode item, String[] countFields) {
        for (String field : countFields) {
            JsonNode value = item.get(field);
            if (value != null && !value.isNull()) {
                try {
                    long v = value.asLong();
                    if (v > 0) return v;
                } catch (Exception ignored) {
                }
            }
        }
        return 0;
    }

    private String extractText(JsonNode item, String[] fieldNames) {
        for (String field : fieldNames) {
            JsonNode value = item.get(field);
            if (value != null && !value.isNull()) {
                String text = value.asText().trim();
                if (!text.isBlank()) return text;
            }
        }
        return null;
    }

    private String normalize(String text) {
        return text.trim().replace(" ", "").toLowerCase();
    }

    private List<JsonNode> extractItems(JsonNode root) {
        if (root == null || root.isNull()) return Collections.emptyList();

        String[] paths = {"/BODY/ROW", "/body/row", "/response/body/items/item", "/body/items/item", "/items/item"};
        for (String path : paths) {
            JsonNode node = root.at(path);
            if (!node.isMissingNode() && !node.isNull()) {
                return toList(node);
            }
        }

        log.warn("보정계수 응답에서 items를 찾지 못했습니다.");
        return Collections.emptyList();
    }

    private List<JsonNode> toList(JsonNode node) {
        List<JsonNode> result = new ArrayList<>();
        if (node.isArray()) node.forEach(result::add);
        else if (node.isObject()) result.add(node);
        return result;
    }
}
