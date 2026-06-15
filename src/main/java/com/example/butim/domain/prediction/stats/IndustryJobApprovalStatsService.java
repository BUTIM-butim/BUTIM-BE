package com.example.butim.domain.prediction.stats;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.prediction.client.PredictionDataClient;
import com.example.butim.domain.prediction.csv.IndustryApprovalCsvReader;
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
public class IndustryJobApprovalStatsService {

    private final IndustryApprovalCsvReader industryApprovalCsvReader;
    private final PredictionDataClient predictionDataClient;

    public ApprovalStats calculate(AccidentInfo accidentInfo) {
        String industryName = accidentInfo.getIndustry().getIndustryName();
        String jobName = accidentInfo.getJob().getJobName();

        // 업종별 승인율: CSV (2018-2024 가중 평균)
        double industryRate = industryApprovalCsvReader.getApprovalRate(industryName);

        // 직종별 승인율: 공공 API (OPA252MT_14/24)
        double jobRate = calculateJobApprovalRate(jobName);

        log.info("승인율 계산 완료 - 업종: {}={}, 직종: {}={}", industryName, industryRate, jobName, jobRate);
        return new ApprovalStats(industryRate, jobRate);
    }

    private double calculateJobApprovalRate(String jobName) {
        try {
            JsonNode applyData = predictionDataClient.fetchJobApplyData();
            JsonNode approvalData = predictionDataClient.fetchJobApprovalData();

            long applyCount = sumCountForName(applyData, jobName, JOB_NAME_FIELDS, APPLY_COUNT_FIELDS);
            long approvalCount = sumCountForName(approvalData, jobName, JOB_NAME_FIELDS, APPROVAL_COUNT_FIELDS);

            if (applyCount == 0) {
                log.warn("직종 신청 건수 0 - 직종명: {}", jobName);
                return 0.0;
            }
            return (double) approvalCount / applyCount;
        } catch (Exception e) {
            log.warn("직종별 승인율 API 호출 실패: {}", e.getMessage());
            return 0.0;
        }
    }

    private static final String[] JOB_NAME_FIELDS = {
            "ocpt_nm", "ocptNm"
    };

    // 신청 건수 필드 (OPA252MT_14_INFO 확인 기반)
    private static final String[] APPLY_COUNT_FIELDS = {
            "ia_rcpr_aply_nocs"
    };

    // TODO: 직종별 승인 데이터(OPA252MT_24_INFO) 응답 확인 후 수정
    private static final String[] APPROVAL_COUNT_FIELDS = {
            "ia_grnt_nocs", "ia_aprvl_nocs", "ia_rcpr_aply_nocs"
    };

    private long sumCountForName(JsonNode root, String targetName, String[] nameFields, String[] countFields) {
        List<JsonNode> items = extractItems(root);
        long total = 0;

        for (JsonNode item : items) {
            String itemName = extractText(item, nameFields);
            if (itemName == null) continue;

            if (matchesName(itemName, targetName)) {
                total += extractCount(item, countFields);
            }
        }

        return total;
    }

    private boolean matchesName(String itemName, String targetName) {
        if (itemName == null || targetName == null) return false;
        String a = normalize(itemName);
        String b = normalize(targetName);
        return a.contains(b) || b.contains(a);
    }

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
                String text = value.asText();
                if (text != null && !text.isBlank()) return text.trim();
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

        log.warn("직종별 API 응답에서 items를 찾지 못했습니다.");
        return Collections.emptyList();
    }

    private List<JsonNode> toList(JsonNode node) {
        List<JsonNode> result = new ArrayList<>();
        if (node.isArray()) node.forEach(result::add);
        else if (node.isObject()) result.add(node);
        return result;
    }
}
