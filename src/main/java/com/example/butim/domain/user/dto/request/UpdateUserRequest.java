package com.example.butim.domain.user.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    private String name;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*]).+$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    private String passwordConfirm;

    @Pattern(regexp = "^010\\d{8}$", message = "올바른 전화번호 형식이 아닙니다.")
    private String phoneNumber;

    @NotNull(message = "필수 약관 동의 여부를 입력해주세요.")
    @AssertTrue(message = "필수 약관에 동의해야 합니다.")
    private Boolean termsAgreed;

    @NotNull(message = "알림 수신 동의 여부를 입력해주세요.")
    private Boolean pushAlarmAgreed;
}