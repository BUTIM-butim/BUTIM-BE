package com.example.butim.domain.industry.controller;


import com.example.butim.domain.industry.dto.IndustryResponse;
import com.example.butim.domain.industry.service.IndustryService;
import com.example.butim.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/industries")
@RequiredArgsConstructor
public class IndustryController {

    private final IndustryService industryService;

    @PostMapping("/sync")
    public BaseResponse<String> syncIndustriesAndJobs() {
        industryService.syncIndustriesAndJobs();

        return BaseResponse.success(
                "요청에 성공했습니다.",
                "업종/직종 데이터 동기화가 완료되었습니다."
        );
    }

    @GetMapping
    public BaseResponse<List<IndustryResponse>> getIndustries() {
        return BaseResponse.success(
                "요청에 성공했습니다.",
                industryService.getIndustries()
        );
    }
}