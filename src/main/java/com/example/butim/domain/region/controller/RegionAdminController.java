package com.example.butim.domain.region.controller;

import com.example.butim.global.response.BaseResponse;
import com.example.butim.domain.region.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/regions")
@RequiredArgsConstructor
public class RegionAdminController {

    private final RegionService regionService;

    @Operation(
            summary = "지역 CSV 데이터 동기화",
            description = "법정동코드 CSV 파일에서 지역 데이터를 읽어 DB에 저장합니다."
    )
    @PostMapping("/sync")
    public BaseResponse<String> syncRegions() {
        regionService.syncRegionsFromCsv();

        return BaseResponse.success(
                "지역 CSV 데이터 동기화에 성공했습니다.",
                "OK"
        );
    }
}
