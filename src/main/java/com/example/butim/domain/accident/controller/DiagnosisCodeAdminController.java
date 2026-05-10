package com.example.butim.domain.accident.controller;

import com.example.butim.domain.accident.service.DiagnosisCodeService;
import com.example.butim.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin - DiagnosisCode", description = "주상병코드 관리 API (개발용)")
@RestController
@RequestMapping("/api/admin/diagnosis-codes")
@RequiredArgsConstructor
public class DiagnosisCodeAdminController {

    private final DiagnosisCodeService diagnosisCodeService;

    @Operation(summary = "주상병코드 동기화", description = "CSV 파일에서 주상병코드를 읽어 DB에 저장합니다.")
    @PostMapping("/sync")
    public ResponseEntity<BaseResponse<String>> sync() {
        int count = diagnosisCodeService.sync();
        return ResponseEntity.ok(BaseResponse.success(count + "건의 주상병코드가 저장되었습니다.", null));
    }
}
