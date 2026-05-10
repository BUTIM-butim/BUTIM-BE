package com.example.butim.domain.accident.dto.response;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.industry.dto.IndustryResponse;
import com.example.butim.domain.industry.dto.JobResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class AccidentInfoResponse {

    private Long id;
    private Integer age;
    private String gender;
    private LocalDate accidentDate;
    private DiagnosisCodeResponse diagnosisCode;
    private IndustryResponse industry;
    private JobResponse job;
    private String businessSize;
    private String employmentType;
    private String additionalInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AccidentInfoResponse from(AccidentInfo accidentInfo) {
        return AccidentInfoResponse.builder()
                .id(accidentInfo.getId())
                .age(accidentInfo.getAge())
                .gender(accidentInfo.getGender().getLabel())
                .accidentDate(accidentInfo.getAccidentDate())
                .diagnosisCode(accidentInfo.getDiagnosisCode() != null
                        ? DiagnosisCodeResponse.from(accidentInfo.getDiagnosisCode())
                        : null)
                .industry(IndustryResponse.from(accidentInfo.getIndustry()))
                .job(JobResponse.from(accidentInfo.getJob()))
                .businessSize(accidentInfo.getBusinessSize().getLabel())
                .employmentType(accidentInfo.getEmploymentType().getLabel())
                .additionalInfo(accidentInfo.getAdditionalInfo())
                .createdAt(accidentInfo.getCreatedAt())
                .updatedAt(accidentInfo.getUpdatedAt())
                .build();
    }
}
