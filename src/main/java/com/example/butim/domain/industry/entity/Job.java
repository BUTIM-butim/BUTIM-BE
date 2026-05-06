package com.example.butim.domain.industry.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long id;

    @Column(name = "job_name", nullable = false)
    private String jobName;

    @Column(name = "job_code", nullable = false, unique = true)
    private String jobCode;

    @Builder
    public Job(String jobName, String jobCode) {
        this.jobName = jobName;
        this.jobCode = jobCode;
    }

    public void updateJobName(String jobName) {
        this.jobName = jobName;
    }
}