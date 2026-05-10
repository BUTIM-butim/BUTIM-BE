package com.example.butim.domain.accident.dto.request;

import com.example.butim.domain.accident.entity.BusinessSize;
import com.example.butim.domain.accident.entity.EmploymentType;
import com.example.butim.domain.accident.entity.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateAccidentInfoRequest {

    @NotNull(message = "나이는 필수입니다.")
    private Integer age;

    @NotNull(message = "성별은 필수입니다.")
    private Gender gender;

    @NotNull(message = "사고발생일은 필수입니다.")
    private LocalDate accidentDate;

    // null이면 "잘 모르겠어요" 선택
    private Long diagnosisCodeId;

    @NotNull(message = "업종은 필수입니다.")
    private Long industryId;

    @NotNull(message = "직종은 필수입니다.")
    private Long jobId;

    @NotNull(message = "사업장 규모는 필수입니다.")
    private BusinessSize businessSize;

    @NotNull(message = "고용 형태는 필수입니다.")
    private EmploymentType employmentType;

    @Size(max = 300, message = "추가 정보는 최대 300자까지 입력할 수 있습니다.")
    private String additionalInfo;
}
