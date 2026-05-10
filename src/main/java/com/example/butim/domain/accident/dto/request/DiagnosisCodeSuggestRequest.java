package com.example.butim.domain.accident.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DiagnosisCodeSuggestRequest {

    @NotBlank(message = "부상 설명은 필수입니다.")
    private String keyword;
}
