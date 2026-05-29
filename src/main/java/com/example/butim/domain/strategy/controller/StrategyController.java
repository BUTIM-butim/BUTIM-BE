package com.example.butim.domain.strategy.controller;

import com.example.butim.domain.strategy.dto.request.CashflowRecalculateRequest;
import com.example.butim.domain.strategy.dto.request.StrategyConfirmRequest;
import com.example.butim.domain.strategy.dto.request.StrategyRunRequest;
import com.example.butim.domain.strategy.dto.response.CashflowResponse;
import com.example.butim.domain.strategy.dto.response.StrategyMeResponse;
import com.example.butim.domain.strategy.dto.response.StrategyRunResponse;
import com.example.butim.domain.strategy.service.StrategyService;
import com.example.butim.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/strategies")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyService strategyService;

    @Operation(
            summary = "전략 추천 실행",
            description = "공공데이터 API 후보를 조회한 뒤 gpt-4o-mini로 맞춤 전략 2개를 생성합니다."
    )
    @PostMapping
    public BaseResponse<StrategyRunResponse> runStrategy(
            @Valid @RequestBody StrategyRunRequest request
    ) {
        return BaseResponse.success("전략 추천이 완료되었습니다.", strategyService.runStrategy(request));
    }

    @Operation(
            summary = "전략 추천 결과 조회",
            description = "사용자의 최근 전략 추천 결과를 조회합니다. 선택 전/선택 후 화면 모두 이 API를 사용합니다."
    )
    @GetMapping("/me")
    public BaseResponse<StrategyMeResponse> getMyStrategy(
            @RequestParam Long userId
    ) {
        return BaseResponse.success("전략 추천 결과 조회에 성공했습니다.", strategyService.getMyStrategy(userId));
    }

    @Operation(
            summary = "전략 확정",
            description = "AI가 추천한 전략 2개 중 사용자가 선택한 전략을 확정합니다."
    )
    @PatchMapping("/me/confirm")
    public BaseResponse<StrategyMeResponse> confirmStrategy(
            @Valid @RequestBody StrategyConfirmRequest request
    ) {
        return BaseResponse.success("전략 선택이 완료되었습니다.", strategyService.confirmStrategy(request));
    }

    @Operation(
            summary = "현금흐름 조회",
            description = "확정된 전략 기준의 현금흐름 그래프와 자산 변동 타임라인을 조회합니다."
    )
    @GetMapping("/me/cashflow")
    public BaseResponse<CashflowResponse> getCashflow(
            @RequestParam Long userId
    ) {
        return BaseResponse.success("현금흐름 조회에 성공했습니다.", strategyService.getCashflow(userId));
    }

    @Operation(
            summary = "현금흐름 재계산",
            description = "지원금 신청/수령, 병원비, 보험금, 현재 자산 변화를 반영해 현금흐름을 다시 계산합니다."
    )
    @PostMapping("/me/cashflow/recalculate")
    public BaseResponse<CashflowResponse> recalculateCashflow(
            @Valid @RequestBody CashflowRecalculateRequest request
    ) {
        return BaseResponse.success("현금흐름 재계산이 완료되었습니다.", strategyService.recalculateCashflow(request));
    }
}