package com.example.butim.domain.accident.repository;

import com.example.butim.domain.accident.entity.DiagnosisCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiagnosisCodeRepository extends JpaRepository<DiagnosisCode, Long> {

    boolean existsByCode(String code);

    List<DiagnosisCode> findByNameContaining(String keyword);

    @Query("""
            SELECT d FROM DiagnosisCode d
            WHERE d.name LIKE %:bodyPart% OR d.name LIKE %:injuryType%
            ORDER BY
                CASE
                    WHEN d.name LIKE %:bodyPart% AND d.name LIKE %:injuryType% THEN 0
                    WHEN d.name LIKE %:bodyPart% THEN 1
                    ELSE 2
                END
            """)
    List<DiagnosisCode> findTop3ByKeywords(@Param("bodyPart") String bodyPart,
                                           @Param("injuryType") String injuryType,
                                           Pageable pageable);
}
