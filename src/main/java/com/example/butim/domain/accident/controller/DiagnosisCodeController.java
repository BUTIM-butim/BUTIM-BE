package com.example.butim.domain.accident.controller;

import com.example.butim.domain.accident.dto.request.DiagnosisCodeSuggestRequest;
import com.example.butim.domain.accident.dto.response.DiagnosisCodeResponse;
import com.example.butim.domain.accident.service.DiagnosisCodeGptService;
import com.example.butim.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "DiagnosisCode", description = "주상병코드 API")
@RestController
@RequestMapping("/api/diagnosis-codes")
@RequiredArgsConstructor
public class DiagnosisCodeController {

    private final DiagnosisCodeGptService diagnosisCodeGptService;

    @Operation(summary = "주상병코드 추천", description = "부상 설명을 입력하면 관련 주상병코드 3개를 추천합니다.")
    @PostMapping("/suggest")
    public ResponseEntity<BaseResponse<List<DiagnosisCodeResponse>>> suggest(
            @Valid @RequestBody DiagnosisCodeSuggestRequest request) {
        return ResponseEntity.ok(
                BaseResponse.success("주상병코드 추천에 성공했습니다.", diagnosisCodeGptService.suggest(request.getKeyword())));
    }
}
