package com.example.butim.domain.accident.csv;

import com.example.butim.domain.accident.entity.DiagnosisCode;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DiagnosisCodeCsvImporter {

    public List<DiagnosisCode> readFromCsv() {
        // code → name 순서 보장, 중복 코드 시 첫 번째 항목 유지
        Map<String, DiagnosisCode> codeMap = new LinkedHashMap<>();

        try {
            ClassPathResource resource = new ClassPathResource("data/diagnosis_code.csv");

            try (CSVReader csvReader = new CSVReader(
                    new InputStreamReader(resource.getInputStream(), Charset.forName("EUC-KR"))
            )) {
                List<String[]> rows = csvReader.readAll();

                // 첫 줄은 헤더 (연번,주상병코드,주상병명,요양총일수,성별,재해당시연령)
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    if (row.length < 3) continue;

                    String code = row[1].trim();
                    String name = row[2].trim();

                    if (code.isEmpty() || name.isEmpty()) continue;

                    codeMap.putIfAbsent(code, DiagnosisCode.builder()
                            .code(code)
                            .name(name)
                            .build());
                }
            }

        } catch (Exception e) {
            log.error("주상병코드 CSV 파일을 읽는 중 오류 발생", e);
            throw new RuntimeException("주상병코드 CSV 파일을 읽는 중 오류가 발생했습니다.");
        }

        return new ArrayList<>(codeMap.values());
    }
}
