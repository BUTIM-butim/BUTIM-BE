package com.example.butim.domain.prediction.csv;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 요양종결자 주상병별 현황정보 CSV 로더 (2022)
 * 컬럼: 순번, 주상병코드, 주상병명, 요양종결인원수, 성별, 해당대상요양일수
 */
@Slf4j
@Component
public class DiagnosisTreatmentCsvReader {

    private static final String CSV_PATH = "data/diagnosis_treatment.csv";

    // 주상병코드 -> TreatmentStat
    private final Map<String, TreatmentStat> statByCode = new HashMap<>();

    @Getter
    public static class TreatmentStat {
        private final long totalPatients;
        private final double weightedAvgDays;
        private final int minDays;
        private final int maxDays;

        public TreatmentStat(long totalPatients, double weightedAvgDays, int minDays, int maxDays) {
            this.totalPatients = totalPatients;
            this.weightedAvgDays = weightedAvgDays;
            this.minDays = minDays;
            this.maxDays = maxDays;
        }

        public int medianDays() {
            return (int) Math.round(weightedAvgDays);
        }
    }

    @PostConstruct
    public void load() {
        // 코드 -> {인원수 가중 일수합, 총 인원수, 최소일수, 최대일수}
        Map<String, long[]> accumulator = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new ClassPathResource(CSV_PATH).getInputStream(),
                        Charset.forName("EUC-KR")))) {

            reader.readLine(); // 헤더 스킵

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isBlank()) continue;

                // 큰따옴표 포함 필드 처리
                String[] parts = parseCsvLine(line);
                if (parts.length < 6) continue;

                try {
                    String code = parts[1].trim().toUpperCase();
                    long patientCount = Long.parseLong(parts[3].trim().replace(",", ""));
                    int days = Integer.parseInt(parts[5].trim().replace(",", ""));

                    if (code.isBlank() || patientCount <= 0 || days <= 0) continue;

                    accumulator.computeIfAbsent(code, k -> new long[]{0, 0, Integer.MAX_VALUE, 0});
                    long[] acc = accumulator.get(code);
                    acc[0] += patientCount * days; // 가중 일수합
                    acc[1] += patientCount;         // 총 인원수
                    acc[2] = Math.min(acc[2], days); // 최소
                    acc[3] = Math.max(acc[3], days); // 최대

                } catch (NumberFormatException e) {
                    log.debug("주상병별 요양일수 CSV 파싱 스킵: {}", line);
                }
            }

            accumulator.forEach((code, acc) -> {
                if (acc[1] > 0) {
                    double weightedAvg = (double) acc[0] / acc[1];
                    statByCode.put(code, new TreatmentStat(acc[1], weightedAvg,
                            (int) acc[2], (int) acc[3]));
                }
            });

            log.info("주상병별 요양일수 CSV 로드 완료 - {}개 코드", statByCode.size());

        } catch (Exception e) {
            log.error("주상병별 요양일수 CSV 로드 실패", e);
        }
    }

    public Optional<TreatmentStat> getStat(String diagnosisCode) {
        if (diagnosisCode == null) return Optional.empty();
        return Optional.ofNullable(statByCode.get(diagnosisCode.trim().toUpperCase()));
    }

    private String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }
}
