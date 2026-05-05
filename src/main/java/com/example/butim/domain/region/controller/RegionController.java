package com.example.butim.domain.region.controller;

import com.example.butim.global.response.BaseResponse;
import com.example.butim.domain.region.dto.SidoResponse;
import com.example.butim.domain.region.dto.SigunguResponse;
import com.example.butim.domain.region.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @Operation(summary = "시/도 목록 조회", description = "전체 시/도 목록을 조회합니다.")
    @GetMapping("/sido")
    public BaseResponse<List<SidoResponse>> getSidoList() {
        return BaseResponse.success(
                "시/도 목록 조회에 성공했습니다.",
                regionService.getSidoList()
        );
    }

    @Operation(summary = "시/군/구 목록 조회", description = "시/도 코드를 기준으로 시/군/구 목록을 조회합니다.")
    @GetMapping("/sigungu")
    public BaseResponse<List<SigunguResponse>> getSigunguList(
            @RequestParam String sidoCode
    ) {
        return BaseResponse.success(
                "시/군/구 목록 조회에 성공했습니다.",
                regionService.getSigunguList(sidoCode)
        );
    }
}