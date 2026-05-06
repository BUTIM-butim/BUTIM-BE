package com.example.butim.domain.auth.controller;

import com.example.butim.domain.auth.dto.request.LoginRequest;
import com.example.butim.domain.auth.dto.request.SignupRequest;
import com.example.butim.domain.auth.dto.response.LoginResponse;
import com.example.butim.domain.auth.dto.response.TokenResponse;
import com.example.butim.domain.auth.service.AuthService;
import com.example.butim.global.response.BaseResponse;
import com.example.butim.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.butim.domain.auth.dto.request.RefreshRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "이름, 이메일, 비밀번호, 전화번호로 회원가입합니다. 전화번호 인증 완료 후 요청해야 합니다.")
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Void>> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.success(201, "회원가입이 완료되었습니다.", null));
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다. Access Token과 Refresh Token이 응답 바디로 반환됩니다.")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(BaseResponse.success("로그인에 성공했습니다.", authService.login(request)));
    }

    @Operation(summary = "로그아웃", description = "로그아웃합니다. Refresh Token 삭제 및 Access Token 블랙리스트 등록. 인증 필요.")
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader("Authorization") String authorization) {
        String accessToken = authorization.substring(7);
        authService.logout(userDetails.getUserId(), accessToken);
        return ResponseEntity.ok(BaseResponse.success("로그아웃되었습니다.", null));
    }

    @Operation(summary = "토큰 갱신", description = "Refresh Token으로 새로운 Access Token과 Refresh Token을 발급받습니다. (Rotation 적용)")
    @PostMapping("/refresh")
    public ResponseEntity<BaseResponse<TokenResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(BaseResponse.success("토큰이 갱신되었습니다.", authService.refresh(request.getRefreshToken())));
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴합니다. 계정은 soft delete 처리됩니다. 인증 필요.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<BaseResponse<Void>> withdraw(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.withdraw(userDetails.getUserId());
        return ResponseEntity.ok(BaseResponse.success("회원탈퇴가 완료되었습니다.", null));
    }
}