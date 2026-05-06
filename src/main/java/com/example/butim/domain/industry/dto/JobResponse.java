package com.example.butim.domain.industry.dto;

import com.example.butim.domain.industry.entity.Job;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobResponse {

    private Long jobId;
    private String jobName;
    private String jobCode;

    public static JobResponse from(Job job) {
        return JobResponse.builder()
                .jobId(job.getId())
                .jobName(job.getJobName())
                .jobCode(job.getJobCode())
                .build();
    }
}