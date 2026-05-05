package com.example.butim.domain.region.csv;

import com.example.butim.domain.region.entity.Region;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RegionCsvImporter {

    public List<Region> readRegionsFromCsv() {
        List<Region> regions = new ArrayList<>();

        try {
            ClassPathResource resource = new ClassPathResource("data/lawd_code.csv");

            try (CSVReader csvReader = new CSVReader(
                    new InputStreamReader(resource.getInputStream(), Charset.forName("CP949"))
            )) {
                List<String[]> rows = csvReader.readAll();

                // 첫 줄은 헤더라서 제외
                for (int i = 1; i < rows.size(); i++) {
                    String[] row = rows.get(i);

                    String lawdCode = row[0].trim();
                    String lawdName = row[1].trim();
                    String existYn = row.length >= 3 ? row[2].trim() : "";

                    // 폐지된 코드 제외
                    if (existYn.contains("폐지")) {
                        continue;
                    }

                    if (lawdCode.length() < 10) {
                        continue;
                    }

                    String sidoCode = lawdCode.substring(0, 2);
                    String sigunguCode = lawdCode.substring(0, 5);

                    // 시도: 1100000000 서울특별시
                    if (lawdCode.substring(2).equals("00000000")) {
                        regions.add(Region.builder()
                                .sidoCode(sidoCode)
                                .sidoName(lawdName)
                                .sigunguCode(null)
                                .sigunguName(null)
                                .build());
                    }

                    // 시군구: 1168000000 서울특별시 강남구
                    else if (lawdCode.substring(5).equals("00000")) {
                        String[] names = lawdName.split(" ");

                        if (names.length < 2) {
                            continue;
                        }

                        String sidoName = names[0];
                        String sigunguName = lawdName.replace(sidoName, "").trim();

                        regions.add(Region.builder()
                                .sidoCode(sidoCode)
                                .sidoName(sidoName)
                                .sigunguCode(sigunguCode)
                                .sigunguName(sigunguName)
                                .build());
                    }
                }
            }

        } catch (Exception e) {
            log.error("법정동코드 CSV 파일을 읽는 중 오류 발생", e);
            throw new RuntimeException("법정동코드 CSV 파일을 읽는 중 오류가 발생했습니다.");
        }

        return regions;
    }
}