package com.example.butim.domain.prediction.controller;

import com.example.butim.domain.prediction.dto.response.PredictionResponse;
import com.example.butim.domain.prediction.service.PredictionService;
import com.example.butim.global.response.BaseResponse;
import com.example.butim.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Prediction", description = "산재 승인 기간 예측 API")
@RestController
@RequestMapping("/api/predictions")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    @Operation(summary = "산재 승인 기간 예측", description = "산재정보를 기반으로 AI가 요양 기간을 예측합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<PredictionResponse>> predict(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        PredictionResponse response = predictionService.predict(userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "예측이 완료되었습니다.", response));
    }

    @Operation(summary = "예측 결과 조회", description = "로그인한 사용자의 가장 최근 예측 결과를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<PredictionResponse>> getMe(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                BaseResponse.success("예측 결과 조회에 성공했습니다.",
                        predictionService.getMe(userDetails.getUserId())));
    }
}
