package com.example.butim.domain.industry.repository;

import com.example.butim.domain.industry.entity.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndustryRepository extends JpaRepository<Industry, Long> {

    Optional<Industry> findByIndustryCode(String industryCode);

    List<Industry> findAllByOrderByIndustryNameAsc();
}