package com.example.butim.domain.accident.repository;

import com.example.butim.domain.accident.entity.DiagnosisCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosisCodeRepository extends JpaRepository<DiagnosisCode, Long> {

    boolean existsByCode(String code);

    List<DiagnosisCode> findByNameContaining(String keyword);
}
