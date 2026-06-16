package com.example.butim.domain.user.controller;

import com.example.butim.domain.user.dto.request.UpdateUserRequest;
import com.example.butim.domain.user.dto.response.UserCurrentStepResponse;
import com.example.butim.domain.user.dto.response.UserMainResponse;
import com.example.butim.domain.user.dto.response.UserMeResponse;
import com.example.butim.domain.user.service.UserService;
import com.example.butim.global.response.BaseResponse;
import com.example.butim.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 이름, 이메일, 전화번호를 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserMeResponse>> getMe(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(BaseResponse.success("내 정보 조회에 성공했습니다.", userService.getMe(userDetails.getUserId())));
    }

    @Operation(summary = "메인 페이지 조회", description = "메인 페이지에 필요한 예측 기간 및 전략 정보를 반환합니다.")
    @GetMapping("/main")
    public ResponseEntity<BaseResponse<UserMainResponse>> getMain(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(BaseResponse.success("메인 페이지 조회에 성공했습니다.",
                userService.getMain(userDetails.getUserId())));
    }

    @Operation(summary = "현재 진행 단계 조회", description = "사용자의 현재 진행 단계를 반환합니다.")
    @GetMapping("/current-step")
    public ResponseEntity<BaseResponse<UserCurrentStepResponse>> getCurrentStep(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(BaseResponse.success("현재 단계 조회에 성공했습니다.",
                userService.getCurrentStep(userDetails.getUserId())));
    }

    @Operation(summary = "내 정보 수정", description = "이름, 이메일, 비밀번호, 전화번호를 수정합니다. 전화번호 변경 시 인증 필요. 약관 동의 필수.")
    @PatchMapping("/me")
    public ResponseEntity<BaseResponse<Void>> updateMe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateUserRequest request) {
        userService.updateMe(userDetails.getUserId(), request);
        return ResponseEntity.ok(BaseResponse.success("내 정보가 수정되었습니다.", null));
    }
}