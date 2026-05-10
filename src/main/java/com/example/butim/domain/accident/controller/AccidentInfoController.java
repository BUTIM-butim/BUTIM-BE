package com.example.butim.domain.accident.controller;

import com.example.butim.domain.accident.dto.request.CreateAccidentInfoRequest;
import com.example.butim.domain.accident.dto.request.UpdateAccidentInfoRequest;
import com.example.butim.domain.accident.dto.response.AccidentInfoResponse;
import com.example.butim.domain.accident.service.AccidentInfoService;
import com.example.butim.global.response.BaseResponse;
import com.example.butim.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AccidentInfo", description = "산재정보 API")
@RestController
@RequestMapping("/api/accident-info")
@RequiredArgsConstructor
public class AccidentInfoController {

    private final AccidentInfoService accidentInfoService;

    @Operation(summary = "산재정보 저장", description = "로그인한 사용자의 산재정보를 저장합니다.")
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateAccidentInfoRequest request) {
        accidentInfoService.create(userDetails.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "산재정보가 저장되었습니다.", null));
    }

    @Operation(summary = "산재정보 조회", description = "로그인한 사용자의 산재정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<AccidentInfoResponse>> getMe(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(
                BaseResponse.success("산재정보 조회에 성공했습니다.", accidentInfoService.getMe(userDetails.getUserId())));
    }

    @Operation(summary = "산재정보 수정", description = "로그인한 사용자의 산재정보를 수정합니다.")
    @PutMapping("/me")
    public ResponseEntity<BaseResponse<Void>> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateAccidentInfoRequest request) {
        accidentInfoService.update(userDetails.getUserId(), request);
        return ResponseEntity.ok(BaseResponse.success("산재정보가 수정되었습니다.", null));
    }
}
