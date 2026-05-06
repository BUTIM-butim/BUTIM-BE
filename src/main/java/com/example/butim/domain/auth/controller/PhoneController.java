package com.example.butim.domain.auth.controller;

import com.example.butim.domain.auth.dto.request.PhoneSendRequest;
import com.example.butim.domain.auth.dto.request.PhoneVerifyRequest;
import com.example.butim.domain.auth.dto.response.PhoneSendResponse;
import com.example.butim.domain.auth.service.PhoneVerificationService;
import com.example.butim.global.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/phone")
@RequiredArgsConstructor
public class PhoneController {

    private final PhoneVerificationService phoneVerificationService;

    @PostMapping("/send")
    public ResponseEntity<BaseResponse<PhoneSendResponse>> send(@Valid @RequestBody PhoneSendRequest request) {
        String code = phoneVerificationService.sendCode(request.phoneNumber());
        return ResponseEntity.ok(BaseResponse.success("인증번호가 발송되었습니다.", new PhoneSendResponse(code)));
    }

    @PostMapping("/verify")
    public ResponseEntity<BaseResponse<Void>> verify(@Valid @RequestBody PhoneVerifyRequest request) {
        phoneVerificationService.verifyCode(request.phoneNumber(), request.code());
        return ResponseEntity.ok(BaseResponse.success("인증이 완료되었습니다.", null));
    }
}