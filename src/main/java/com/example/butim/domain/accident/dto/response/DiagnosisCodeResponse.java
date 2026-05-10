package com.example.butim.domain.accident.dto.response;

import com.example.butim.domain.accident.entity.DiagnosisCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiagnosisCodeResponse {

    private Long id;
    private String code;
    private String name;

    public static DiagnosisCodeResponse from(DiagnosisCode diagnosisCode) {
        return DiagnosisCodeResponse.builder()
                .id(diagnosisCode.getId())
                .code(diagnosisCode.getCode())
                .name(diagnosisCode.getName())
                .build();
    }
}
