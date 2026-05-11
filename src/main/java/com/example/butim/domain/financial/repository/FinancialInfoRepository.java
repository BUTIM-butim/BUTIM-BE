package com.example.butim.domain.financial.repository;

import com.example.butim.domain.financial.entity.FinancialInfo;
import com.example.butim.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialInfoRepository extends JpaRepository<FinancialInfo, Long> {

    Optional<FinancialInfo> findByUser(User user);

    boolean existsByUser(User user);
}