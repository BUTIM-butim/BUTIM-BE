package com.example.butim.domain.prediction.stats;

import com.example.butim.domain.prediction.csv.DiagnosisTreatmentCsvReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnosisStatsService {

    private final DiagnosisTreatmentCsvReader diagnosisTreatmentCsvReader;

    public DiagnosisStats calculate(String diagnosisCode) {
        return diagnosisTreatmentCsvReader.getStat(diagnosisCode)
                .map(stat -> {
                    log.info("주상병 요양일수 통계 - code: {}, avg: {}, min: {}, max: {}, patients: {}",
                            diagnosisCode, stat.getWeightedAvgDays(),
                            stat.getMinDays(), stat.getMaxDays(), stat.getTotalPatients());
                    return new DiagnosisStats(
                            stat.getMinDays(),
                            stat.getMaxDays(),
                            stat.medianDays(),
                            (int) Math.min(stat.getTotalPatients(), Integer.MAX_VALUE)
                    );
                })
                .orElseGet(() -> {
                    log.warn("주상병코드 데이터 없음 - code: {}", diagnosisCode);
                    return new DiagnosisStats(0, 0, 0, 0);
                });
    }
}
