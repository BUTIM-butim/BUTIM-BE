package com.example.butim.domain.accident.service;

import com.example.butim.domain.accident.csv.DiagnosisCodeCsvImporter;
import com.example.butim.domain.accident.entity.DiagnosisCode;
import com.example.butim.domain.accident.repository.DiagnosisCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnosisCodeService {

    private final DiagnosisCodeRepository diagnosisCodeRepository;
    private final DiagnosisCodeCsvImporter csvImporter;

    @Transactional
    public int sync() {
        List<DiagnosisCode> all = csvImporter.readFromCsv();

        int count = 0;
        for (DiagnosisCode diagnosisCode : all) {
            if (!diagnosisCodeRepository.existsByCode(diagnosisCode.getCode())) {
                diagnosisCodeRepository.save(diagnosisCode);
                count++;
            }
        }

        log.info("주상병코드 동기화 완료: {}건 저장", count);
        return count;
    }

    @Transactional(readOnly = true)
    public List<DiagnosisCode> searchByKeyword(String keyword) {
        return diagnosisCodeRepository.findByNameContaining(keyword);
    }
}
