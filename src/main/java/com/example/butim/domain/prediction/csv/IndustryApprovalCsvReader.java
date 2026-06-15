package com.example.butim.domain.prediction.csv;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 업종별 산재신청 승인현황 CSV 로더
 * 컬럼: 연도, 업종명, 신청, 승인, 승인율(퍼센트)
 * 최근 연도에 높은 가중치를 적용한 업종별 승인율을 캐시합니다.
 */
@Slf4j
@Component
public class IndustryApprovalCsvReader {

    private static final String CSV_PATH = "data/industry_approval.csv";
    private static final int BASE_YEAR = 2018;

    // 업종명(정규화) -> 가중 승인율
    private final Map<String, Double> approvalRateByIndustry = new HashMap<>();

    @PostConstruct
    public void load() {
        // 업종명 -> {가중합계, 가중치합계}
        Map<String, double[]> accumulator = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource(CSV_PATH).getInputStream(),
                        Charset.forName("EUC-KR")))) {

            reader.readLine(); // 헤더 스킵

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isBlank()) continue;

                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                try {
                    int year = Integer.parseInt(parts[0].trim());
                    String industryName = normalize(parts[1].trim());
                    double approvalRate = Double.parseDouble(parts[4].trim());

                    if (approvalRate <= 0) continue;

                    // 최근 연도일수록 높은 가중치 (2018=1, 2019=2, ..., 2024=7)
                    double weight = (year - BASE_YEAR) + 1;

                    accumulator.computeIfAbsent(industryName, k -> new double[2]);
                    accumulator.get(industryName)[0] += approvalRate * weight;
                    accumulator.get(industryName)[1] += weight;

                } catch (NumberFormatException e) {
                    log.debug("업종별 승인율 CSV 파싱 스킵: {}", line);
                }
            }

            accumulator.forEach((name, values) ->
                    approvalRateByIndustry.put(name, values[0] / values[1] / 100.0)
            );

            log.info("업종별 승인율 CSV 로드 완료 - {}개 업종", approvalRateByIndustry.size());

        } catch (Exception e) {
            log.error("업종별 승인율 CSV 로드 실패", e);
        }
    }

    /**
     * 업종명으로 가중 승인율 조회 (0.0 ~ 1.0)
     * 매칭 실패 시 0.0 반환
     */
    public double getApprovalRate(String industryName) {
        String normalized = normalize(industryName);

        // 정확히 일치
        if (approvalRateByIndustry.containsKey(normalized)) {
            return approvalRateByIndustry.get(normalized);
        }

        // 부분 일치 (DB 업종명이 CSV 업종명을 포함하거나 그 반대)
        for (Map.Entry<String, Double> entry : approvalRateByIndustry.entrySet()) {
            if (normalized.contains(entry.getKey()) || entry.getKey().contains(normalized)) {
                return entry.getValue();
            }
        }

        log.warn("업종별 승인율 매칭 실패 - 업종명: {}", industryName);
        return 0.0;
    }

    public Map<String, Double> getAll() {
        return Map.copyOf(approvalRateByIndustry);
    }

    private String normalize(String text) {
        return text.replace(" ", "").toLowerCase();
    }
}
