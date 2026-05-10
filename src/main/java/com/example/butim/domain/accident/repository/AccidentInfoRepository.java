package com.example.butim.domain.accident.repository;

import com.example.butim.domain.accident.entity.AccidentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccidentInfoRepository extends JpaRepository<AccidentInfo, Long> {

    boolean existsByUserId(Long userId);

    Optional<AccidentInfo> findByUserId(Long userId);
}
