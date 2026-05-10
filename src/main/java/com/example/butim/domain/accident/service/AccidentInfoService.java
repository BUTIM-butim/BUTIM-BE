package com.example.butim.domain.accident.service;

import com.example.butim.domain.accident.dto.request.CreateAccidentInfoRequest;
import com.example.butim.domain.accident.dto.request.UpdateAccidentInfoRequest;
import com.example.butim.domain.accident.dto.response.AccidentInfoResponse;
import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.accident.entity.DiagnosisCode;
import com.example.butim.domain.accident.repository.AccidentInfoRepository;
import com.example.butim.domain.accident.repository.DiagnosisCodeRepository;
import com.example.butim.domain.industry.entity.Industry;
import com.example.butim.domain.industry.entity.Job;
import com.example.butim.domain.industry.repository.IndustryRepository;
import com.example.butim.domain.industry.repository.JobRepository;
import com.example.butim.domain.user.entity.User;
import com.example.butim.domain.user.repository.UserRepository;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccidentInfoService {

    private final AccidentInfoRepository accidentInfoRepository;
    private final DiagnosisCodeRepository diagnosisCodeRepository;
    private final UserRepository userRepository;
    private final IndustryRepository industryRepository;
    private final JobRepository jobRepository;

    @Transactional
    public void create(Long userId, CreateAccidentInfoRequest request) {
        if (accidentInfoRepository.existsByUserId(userId)) {
            throw new CustomException(ErrorCode.ACCIDENT_INFO_ALREADY_EXISTS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Industry industry = industryRepository.findById(request.getIndustryId())
                .orElseThrow(() -> new CustomException(ErrorCode.INDUSTRY_NOT_FOUND));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_NOT_FOUND));

        DiagnosisCode diagnosisCode = null;
        if (request.getDiagnosisCodeId() != null) {
            diagnosisCode = diagnosisCodeRepository.findById(request.getDiagnosisCodeId())
                    .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_CODE_NOT_FOUND));
        }

        AccidentInfo accidentInfo = AccidentInfo.builder()
                .user(user)
                .age(request.getAge())
                .gender(request.getGender())
                .accidentDate(request.getAccidentDate())
                .diagnosisCode(diagnosisCode)
                .industry(industry)
                .job(job)
                .businessSize(request.getBusinessSize())
                .employmentType(request.getEmploymentType())
                .additionalInfo(request.getAdditionalInfo())
                .build();

        accidentInfoRepository.save(accidentInfo);
    }

    @Transactional(readOnly = true)
    public AccidentInfoResponse getMe(Long userId) {
        AccidentInfo accidentInfo = accidentInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCIDENT_INFO_NOT_FOUND));

        return AccidentInfoResponse.from(accidentInfo);
    }

    @Transactional
    public void update(Long userId, UpdateAccidentInfoRequest request) {
        AccidentInfo accidentInfo = accidentInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCIDENT_INFO_NOT_FOUND));

        Industry industry = industryRepository.findById(request.getIndustryId())
                .orElseThrow(() -> new CustomException(ErrorCode.INDUSTRY_NOT_FOUND));

        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new CustomException(ErrorCode.JOB_NOT_FOUND));

        DiagnosisCode diagnosisCode = null;
        if (request.getDiagnosisCodeId() != null) {
            diagnosisCode = diagnosisCodeRepository.findById(request.getDiagnosisCodeId())
                    .orElseThrow(() -> new CustomException(ErrorCode.DIAGNOSIS_CODE_NOT_FOUND));
        }

        accidentInfo.update(
                request.getAge(),
                request.getGender(),
                request.getAccidentDate(),
                diagnosisCode,
                industry,
                job,
                request.getBusinessSize(),
                request.getEmploymentType(),
                request.getAdditionalInfo()
        );
    }
}
