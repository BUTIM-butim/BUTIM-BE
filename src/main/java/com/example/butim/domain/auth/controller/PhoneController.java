package com.example.butim.domain.auth.controller;

import com.example.butim.domain.auth.dto.request.PhoneSendRequest;
import com.example.butim.domain.auth.dto.request.PhoneVerifyRequest;
import com.example.butim.domain.auth.dto.response.PhoneSendResponse;
import com.example.butim.domain.auth.service.PhoneVerificationService;
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

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth/phone")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneVerificationService phoneVerificationService;

    @Operation(summary = "인증번호 발송", description = "전화번호로 6자리 인증번호를 발송합니다. 인증번호는 3분간 유효합니다. (현재 SMS 미연동 — 응답 바디로 반환)")
    @PostMapping("/send")
    public ResponseEntity<BaseResponse<PhoneSendResponse>> send(@Valid @RequestBody PhoneSendRequest request) {
        String code = phoneVerificationService.sendCode(request.getPhoneNumber());
        return ResponseEntity.ok(BaseResponse.success("인증번호가 발송되었습니다.", new PhoneSendResponse(code)));
    }

    @Operation(summary = "인증번호 확인", description = "발송된 인증번호를 검증합니다. 인증 완료 상태는 10분간 유지됩니다.")
    @PostMapping("/verify")
    public ResponseEntity<BaseResponse<Void>> verify(@Valid @RequestBody PhoneVerifyRequest request) {
        phoneVerificationService.verifyCode(request.getPhoneNumber(), request.getCode());
        return ResponseEntity.ok(BaseResponse.success("인증이 완료되었습니다.", null));
    }
}