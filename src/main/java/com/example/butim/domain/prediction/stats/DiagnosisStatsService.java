package com.example.butim.domain.prediction.stats;

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
public class DiagnosisStatsService {

    private final PredictionDataClient predictionDataClient;

    public DiagnosisStats calculate(String diagnosisCode) {
        JsonNode approvalData = predictionDataClient.fetchDiagnosisApprovalData();
        List<JsonNode> items = extractItems(approvalData);

        List<Integer> daysList = new ArrayList<>();

        for (JsonNode item : items) {
            String itemCode = extractCode(item);
            if (itemCode == null || !matchesCode(itemCode, diagnosisCode)) continue;

            int days = extractDays(item);
            if (days > 0) daysList.add(days);
        }

        if (daysList.isEmpty()) {
            log.warn("주상병코드에 해당하는 요양일수 데이터 없음 - code: {}", diagnosisCode);
            return new DiagnosisStats(0, 0, 0, 0);
        }

        Collections.sort(daysList);
        int min = daysList.get(0);
        int max = daysList.get(daysList.size() - 1);
        int median = daysList.get(daysList.size() / 2);

        log.info("주상병 요양일수 통계 - code: {}, min: {}, max: {}, median: {}, count: {}",
                diagnosisCode, min, max, median, daysList.size());

        return new DiagnosisStats(min, max, median, daysList.size());
    }

    // TODO: 실제 API 응답 확인 후 주상병코드 필드명 수정
    private static final String[] CODE_FIELDS = {
            "dsas_cd", "dsasCd", "diagnosis_cd", "diagnosisCd", "disease_cd", "diseaseCd",
            "sjhb_cd", "sjhbCd", "상병코드", "주상병코드"
    };

    // TODO: 실제 API 응답 확인 후 요양일수 필드명 수정
    private static final String[] DAYS_FIELDS = {
            "yyang_dy_cnt", "yyangDyCnt", "care_day_cnt", "careDayCnt",
            "treat_day", "treatDay", "day_cnt", "dayCnt", "요양일수"
    };

    private String extractCode(JsonNode item) {
        for (String field : CODE_FIELDS) {
            JsonNode value = item.get(field);
            if (value != null && !value.isNull()) {
                String text = value.asText().trim();
                if (!text.isBlank()) return text;
            }
        }
        return null;
    }

    private int extractDays(JsonNode item) {
        for (String field : DAYS_FIELDS) {
            JsonNode value = item.get(field);
            if (value != null && !value.isNull()) {
                try {
                    int days = value.asInt();
                    if (days > 0) return days;
                } catch (Exception ignored) {
                }
            }
        }
        return 0;
    }

    private boolean matchesCode(String itemCode, String targetCode) {
        if (itemCode == null || targetCode == null) return false;
        return itemCode.trim().equalsIgnoreCase(targetCode.trim());
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

        log.warn("주상병 승인 응답에서 items를 찾지 못했습니다.");
        return Collections.emptyList();
    }

    private List<JsonNode> toList(JsonNode node) {
        List<JsonNode> result = new ArrayList<>();
        if (node.isArray()) {
            node.forEach(result::add);
        } else if (node.isObject()) {
            result.add(node);
        }
        return result;
    }
}
