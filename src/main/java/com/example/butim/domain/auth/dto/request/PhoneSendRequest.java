package com.example.butim.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneSendRequest(

        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^010\\d{8}$", message = "올바른 전화번호 형식이 아닙니다.")
        String phoneNumber
) {}