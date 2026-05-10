package com.example.butim.domain.accident.entity;

import com.example.butim.domain.industry.entity.Industry;
import com.example.butim.domain.industry.entity.Job;
import com.example.butim.domain.user.entity.User;
import com.example.butim.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "accident_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccidentInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Column(name = "accident_date", nullable = false)
    private LocalDate accidentDate;

    // null이면 "잘 모르겠어요" 선택
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnosis_code_id")
    private DiagnosisCode diagnosisCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id", nullable = false)
    private Industry industry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_size", nullable = false, length = 20)
    private BusinessSize businessSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", nullable = false, length = 20)
    private EmploymentType employmentType;

    @Column(name = "additional_info", length = 300)
    private String additionalInfo;

    @Builder
    public AccidentInfo(User user, Integer age, Gender gender, LocalDate accidentDate,
                        DiagnosisCode diagnosisCode, Industry industry, Job job,
                        BusinessSize businessSize, EmploymentType employmentType,
                        String additionalInfo) {
        this.user = user;
        this.age = age;
        this.gender = gender;
        this.accidentDate = accidentDate;
        this.diagnosisCode = diagnosisCode;
        this.industry = industry;
        this.job = job;
        this.businessSize = businessSize;
        this.employmentType = employmentType;
        this.additionalInfo = additionalInfo;
    }

    public void update(Integer age, Gender gender, LocalDate accidentDate,
                       DiagnosisCode diagnosisCode, Industry industry, Job job,
                       BusinessSize businessSize, EmploymentType employmentType,
                       String additionalInfo) {
        this.age = age;
        this.gender = gender;
        this.accidentDate = accidentDate;
        this.diagnosisCode = diagnosisCode;
        this.industry = industry;
        this.job = job;
        this.businessSize = businessSize;
        this.employmentType = employmentType;
        this.additionalInfo = additionalInfo;
    }
}
