package com.example.butim.domain.industry.repository;

import com.example.butim.domain.industry.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findByJobCode(String jobCode);

    List<Job> findAllByOrderByJobNameAsc();
}