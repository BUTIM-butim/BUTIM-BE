package com.example.butim.domain.financial.service;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.accident.repository.AccidentInfoRepository;
import com.example.butim.domain.financial.dto.request.FinancialInfoRequest;
import com.example.butim.domain.financial.dto.response.FinancialInfoResponse;
import com.example.butim.domain.financial.entity.FinancialInfo;
import com.example.butim.domain.financial.repository.FinancialInfoRepository;
import com.example.butim.domain.region.entity.Region;
import com.example.butim.domain.region.repository.RegionRepository;
import com.example.butim.domain.user.entity.User;
import com.example.butim.domain.user.repository.UserRepository;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FinancialInfoService {

    private final FinancialInfoRepository financialInfoRepository;
    private final UserRepository userRepository;
    private final AccidentInfoRepository accidentInfoRepository;
    private final RegionRepository regionRepository;

    @Transactional
    public FinancialInfoResponse createFinancialInfo(Long userId, FinancialInfoRequest request) {
        User user = getUser(userId);

        if (financialInfoRepository.existsByUser(user)) {
            throw new CustomException(ErrorCode.FINANCIAL_INFO_ALREADY_EXISTS);
        }

        AccidentInfo accidentInfo = getAccidentInfo(request.getAccidentInfoId());
        Region region = getRegion(request.getRegionId());

        FinancialInfo financialInfo = FinancialInfo.builder()
                .user(user)
                .accidentInfo(accidentInfo)
                .region(region)
                .currentAssets(request.getCurrentAssets())
                .monthlyFixedExpense(request.getMonthlyFixedExpense())
                .incomeLevel(request.getIncomeLevel())
                .householdType(request.getHouseholdType())
                .hasDependent(request.getHasDependent())
                .hasChild(request.getHasChild())
                .childCount(normalizeChildCount(request))
                .isPregnant(request.getIsPregnant())
                .hasDisability(request.getHasDisability())
                .currentEmploymentStatus(request.getCurrentEmploymentStatus())
                .build();

        FinancialInfo savedFinancialInfo = financialInfoRepository.save(financialInfo);

        return FinancialInfoResponse.from(savedFinancialInfo);
    }

    public FinancialInfoResponse getMyFinancialInfo(Long userId) {
        User user = getUser(userId);

        FinancialInfo financialInfo = financialInfoRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.FINANCIAL_INFO_NOT_FOUND));

        return FinancialInfoResponse.from(financialInfo);
    }

    @Transactional
    public FinancialInfoResponse updateMyFinancialInfo(Long userId, FinancialInfoRequest request) {
        User user = getUser(userId);

        FinancialInfo financialInfo = financialInfoRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.FINANCIAL_INFO_NOT_FOUND));

        Region region = getRegion(request.getRegionId());

        financialInfo.update(
                region,
                request.getCurrentAssets(),
                request.getMonthlyFixedExpense(),
                request.getIncomeLevel(),
                request.getHouseholdType(),
                request.getHasDependent(),
                request.getHasChild(),
                normalizeChildCount(request),
                request.getIsPregnant(),
                request.getHasDisability(),
                request.getCurrentEmploymentStatus()
        );

        return FinancialInfoResponse.from(financialInfo);
    }

    private Integer normalizeChildCount(FinancialInfoRequest request) {
        if (Boolean.FALSE.equals(request.getHasChild())) {
            return 0;
        }

        if (request.getChildCount() == null) {
            return 0;
        }

        return request.getChildCount();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private AccidentInfo getAccidentInfo(Long accidentInfoId) {
        return accidentInfoRepository.findById(accidentInfoId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCIDENT_INFO_NOT_FOUND));
    }

    private Region getRegion(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new CustomException(ErrorCode.REGION_NOT_FOUND));
    }
}