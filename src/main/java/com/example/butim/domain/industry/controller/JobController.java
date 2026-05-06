package com.example.butim.domain.industry.controller;

import com.example.butim.domain.industry.dto.JobResponse;
import com.example.butim.domain.industry.service.IndustryService;
import com.example.butim.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final IndustryService industryService;

    @GetMapping
    public BaseResponse<List<JobResponse>> getJobs() {
        return BaseResponse.success(
                "요청에 성공했습니다.",
                industryService.getJobs()
        );
    }
}