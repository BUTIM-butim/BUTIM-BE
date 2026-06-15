package com.example.butim.domain.prediction.stats;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.prediction.client.PrecedentApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrecedentStatsService {

    private final PrecedentApiClient precedentApiClient;

    private static final String KIND_B_CARE = "요양";
    private static final String KIND_C_ACCIDENT = "업무상사고";
    private static final String KIND_C_DISEASE = "업무상질병";

    public PrecedentStats calculate(AccidentInfo accidentInfo) {
        String kindC = resolveKindC(accidentInfo);

        int approvedCount = fetchCount("승인", KIND_B_CARE, kindC);
        int rejectedCount = fetchCount("기각", KIND_B_CARE, kindC);
        List<String> caseContents = fetchContents(KIND_B_CARE, kindC);

        log.info("판례 통계 - kindC: {}, 승인: {}, 기각: {}", kindC, approvedCount, rejectedCount);
        return new PrecedentStats(approvedCount, rejectedCount, caseContents);
    }

    private int fetchCount(String kindA, String kindB, String kindC) {
        try {
            JsonNode response = precedentApiClient.fetchCaseCount(kindA, kindB, kindC);
            // 응답: body.totalCount 또는 body.items.item.cnt
            JsonNode totalCount = response.at("/body/totalCount");
            if (!totalCount.isMissingNode()) return totalCount.asInt();

            JsonNode cnt = response.at("/body/items/item/cnt");
            if (!cnt.isMissingNode()) return cnt.asInt();

            return 0;
        } catch (Exception e) {
            log.warn("판례 건수 조회 실패 - kindA: {}, error: {}", kindA, e.getMessage());
            return 0;
        }
    }

    private List<String> fetchContents(String kindB, String kindC) {
        List<String> contents = new ArrayList<>();
        try {
            JsonNode response = precedentApiClient.fetchCaseContents(kindB, kindC);
            JsonNode items = response.at("/body/items/item");

            if (items.isArray()) {
                items.forEach(item -> {
                    String content = item.path("noncontent").asText("").trim();
                    if (!content.isBlank()) contents.add(content);
                });
            } else if (items.isObject()) {
                String content = items.path("noncontent").asText("").trim();
                if (!content.isBlank()) contents.add(content);
            }
        } catch (Exception e) {
            log.warn("판례 내용 조회 실패 - error: {}", e.getMessage());
        }
        return contents;
    }

    // 진단코드 있으면 업무상질병, 없으면 업무상사고로 분류
    private String resolveKindC(AccidentInfo accidentInfo) {
        return accidentInfo.getDiagnosisCode() != null ? KIND_C_DISEASE : KIND_C_ACCIDENT;
    }
}
