package com.example.butim.domain.financial.controller;

import com.example.butim.domain.financial.dto.request.FinancialInfoRequest;
import com.example.butim.domain.financial.dto.response.FinancialInfoResponse;
import com.example.butim.domain.financial.service.FinancialInfoService;
import com.example.butim.global.response.BaseResponse;
import com.example.butim.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-info")
@RequiredArgsConstructor
public class FinancialInfoController {

    private final FinancialInfoService financialInfoService;

    @PostMapping
    public BaseResponse<FinancialInfoResponse> createFinancialInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody FinancialInfoRequest request
    ) {
        Long userId = userDetails.getUserId();

        FinancialInfoResponse response = financialInfoService.createFinancialInfo(userId, request);

        return new BaseResponse<FinancialInfoResponse>(
                true,
                201,
                "재정정보 저장에 성공했습니다.",
                response
        );
    }

    @GetMapping("/me")
    public BaseResponse<FinancialInfoResponse> getMyFinancialInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUserId();

        FinancialInfoResponse response = financialInfoService.getMyFinancialInfo(userId);

        return new BaseResponse<FinancialInfoResponse>(
                true,
                200,
                "재정정보 조회에 성공했습니다.",
                response
        );
    }

    @PutMapping("/me")
    public BaseResponse<FinancialInfoResponse> updateMyFinancialInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody FinancialInfoRequest request
    ) {
        Long userId = userDetails.getUserId();

        FinancialInfoResponse response = financialInfoService.updateMyFinancialInfo(userId, request);

        return new BaseResponse<FinancialInfoResponse>(
                true,
                200,
                "재정정보 수정에 성공했습니다.",
                response
        );
    }
}