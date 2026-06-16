package com.example.butim.domain.strategy.service;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.accident.repository.AccidentInfoRepository;
import com.example.butim.domain.financial.entity.FinancialInfo;
import com.example.butim.domain.financial.repository.FinancialInfoRepository;
import com.example.butim.domain.prediction.entity.Prediction;
import com.example.butim.domain.prediction.repository.PredictionRepository;
import com.example.butim.domain.region.entity.Region;
import com.example.butim.domain.strategy.dto.common.*;
import com.example.butim.domain.strategy.dto.request.CashflowRecalculateRequest;
import com.example.butim.domain.strategy.dto.request.StrategyConfirmRequest;
import com.example.butim.domain.strategy.dto.request.StrategyRunRequest;
import com.example.butim.domain.strategy.dto.response.CashflowResponse;
import com.example.butim.domain.strategy.dto.response.StrategyMeResponse;
import com.example.butim.domain.strategy.dto.response.StrategyRunResponse;
import com.example.butim.domain.strategy.entity.CashflowSnapshot;
import com.example.butim.domain.strategy.entity.StrategyItem;
import com.example.butim.domain.strategy.entity.StrategyResult;
import com.example.butim.domain.strategy.enums.CashflowEventType;
import com.example.butim.domain.strategy.enums.StrategyItemType;
import com.example.butim.domain.strategy.enums.StrategyType;
import com.example.butim.domain.strategy.external.WelfareApiAggregator;
import com.example.butim.domain.strategy.repository.CashflowSnapshotRepository;
import com.example.butim.domain.strategy.repository.StrategyItemRepository;
import com.example.butim.domain.strategy.repository.StrategyResultRepository;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StrategyService {

    private final StrategyResultRepository strategyResultRepository;
    private final StrategyItemRepository strategyItemRepository;
    private final CashflowSnapshotRepository cashflowSnapshotRepository;
    private final AccidentInfoRepository accidentInfoRepository;
    private final FinancialInfoRepository financialInfoRepository;
    private final PredictionRepository predictionRepository;

    private final WelfareApiAggregator welfareApiAggregator;
    private final StrategyAiService strategyAiService;
    private final CashflowCalculator cashflowCalculator;

    @Transactional
    public StrategyRunResponse runStrategy(StrategyRunRequest request) {
        AccidentInfo accidentInfo = accidentInfoRepository.findById(request.accidentInfoId())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCIDENT_INFO_NOT_FOUND));

        FinancialInfo financialInfo = financialInfoRepository.findById(request.financialInfoId())
                .orElseThrow(() -> new CustomException(ErrorCode.FINANCIAL_INFO_NOT_FOUND));

        Prediction prediction = predictionRepository.findByUserId(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.PREDICTION_NOT_FOUND));

        StrategyContext context = buildContext(accidentInfo, financialInfo, prediction);

        List<CandidateSupportDto> candidates = welfareApiAggregator.collectCandidates(context.regionName());

        AiStrategyResult aiResult = strategyAiService.recommend(candidates, context);

        StrategyResult result = StrategyResult.builder()
                .userId(request.userId())
                .accidentInfoId(request.accidentInfoId())
                .financialInfoId(request.financialInfoId())
                .currentAsset(context.currentAsset())
                .monthlyLivingCost(context.monthlyLivingCost())
                .cashGapDay(context.cashGapDay())
                .approvalExpectedDays(context.approvalExpectedDays())
                .paymentExpectedDays(context.paymentExpectedDays())
                .expectedWorkersCompensationAmount(0)
                .hospitalCost(0)
                .insuranceAmount(0)
                .aiSummary(aiResult.summary())
                .build();

        StrategyResult savedResult = strategyResultRepository.save(result);

        List<StrategyItem> items = toStrategyItems(savedResult.getId(), aiResult, candidates);
        List<StrategyItem> savedItems = strategyItemRepository.saveAll(items);

        List<CashflowSnapshot> snapshots = cashflowCalculator.createInitialSnapshots(savedResult, savedItems);
        List<CashflowSnapshot> savedSnapshots = cashflowSnapshotRepository.saveAll(snapshots);

        return new StrategyRunResponse(
                savedResult.getId(),
                savedResult.getCurrentAsset(),
                savedResult.getCashGapDay(),
                savedResult.getApprovalExpectedDays(),
                savedResult.getPaymentExpectedDays(),
                toStrategyCards(savedItems),
                toCashflowDtos(savedSnapshots)
        );
    }

    public StrategyMeResponse getMyStrategy(Long userId) {
        StrategyResult result = getLatestResult(userId);

        List<StrategyItem> items = strategyItemRepository.findByStrategyResultIdOrderByIdAsc(result.getId());

        List<CashflowSnapshot> snapshots = result.isConfirmed()
                ? cashflowSnapshotRepository.findByStrategyResultIdAndStrategyTypeOrderByDayOffsetAscIdAsc(
                result.getId(),
                result.getSelectedStrategyType()
        )
                : cashflowSnapshotRepository.findByStrategyResultIdOrderByDayOffsetAscIdAsc(result.getId());

        List<StrategyCardDto> cards = toStrategyCards(items);

        StrategyCardDto selected = null;
        if (result.isConfirmed()) {
            selected = cards.stream()
                    .filter(card -> card.strategyType() == result.getSelectedStrategyType())
                    .findFirst()
                    .orElse(null);
        }

        return new StrategyMeResponse(
                result.getId(),
                result.isConfirmed(),
                result.getSelectedStrategyType(),
                result.getCurrentAsset(),
                result.getCashGapDay(),
                result.getApprovalExpectedDays(),
                result.getPaymentExpectedDays(),
                cards,
                selected,
                toCashflowDtos(snapshots),
                toTimelineDtos(snapshots)
        );
    }

    @Transactional
    public StrategyMeResponse confirmStrategy(StrategyConfirmRequest request) {
        StrategyResult result = getLatestResult(request.userId());

        result.confirm(request.selectedStrategyType());

        return getMyStrategy(request.userId());
    }

    public CashflowResponse getCashflow(Long userId) {
        StrategyResult result = getLatestResult(userId);

        if (!result.isConfirmed()) {
            throw new CustomException(ErrorCode.STRATEGY_NOT_CONFIRMED);
        }

        List<CashflowSnapshot> snapshots =
                cashflowSnapshotRepository.findByStrategyResultIdAndStrategyTypeOrderByDayOffsetAscIdAsc(
                        result.getId(),
                        result.getSelectedStrategyType()
                );

        return new CashflowResponse(
                result.getId(),
                result.getCurrentAsset(),
                result.getCashGapDay(),
                toCashflowDtos(snapshots),
                toTimelineDtos(snapshots)
        );
    }

    @Transactional
    public CashflowResponse recalculateCashflow(CashflowRecalculateRequest request) {
        boolean noInput = isEmpty(request.appliedItemIds())
                && isEmpty(request.receivedItemIds())
                && request.hospitalCost() == null
                && request.insuranceAmount() == null
                && request.currentAsset() == null;

        if (noInput) {
            throw new CustomException(ErrorCode.INVALID_CASHFLOW_INPUT);
        }

        StrategyResult result = getLatestResult(request.userId());

        if (!result.isConfirmed()) {
            throw new CustomException(ErrorCode.STRATEGY_NOT_CONFIRMED);
        }

        result.updateCashflowInput(
                request.currentAsset(),
                request.hospitalCost(),
                request.insuranceAmount()
        );

        List<StrategyItem> selectedItems =
                strategyItemRepository.findByStrategyResultIdAndStrategyTypeOrderByIdAsc(
                        result.getId(),
                        result.getSelectedStrategyType()
                );

        cashflowSnapshotRepository.deleteByStrategyResultIdAndStrategyType(
                result.getId(),
                result.getSelectedStrategyType()
        );

        List<CashflowSnapshot> recalculated = cashflowCalculator.recalculateSelectedStrategy(
                result,
                selectedItems,
                nullToEmpty(request.appliedItemIds()),
                nullToEmpty(request.receivedItemIds())
        );

        List<CashflowSnapshot> saved = cashflowSnapshotRepository.saveAll(recalculated);

        return new CashflowResponse(
                result.getId(),
                result.getCurrentAsset(),
                result.getCashGapDay(),
                toCashflowDtos(saved),
                toTimelineDtos(saved)
        );
    }

    private StrategyContext buildContext(AccidentInfo accidentInfo, FinancialInfo financialInfo, Prediction prediction) {
        Region region = financialInfo.getRegion();

        String regionCode = region.getSigunguCode() != null ? region.getSigunguCode() : region.getSidoCode();
        String regionName = region.getSigunguName() != null ? region.getSigunguName() : region.getSidoName();

        String injuryName = accidentInfo.getDiagnosisCode() != null
                ? accidentInfo.getDiagnosisCode().getName()
                : null;

        Integer currentAsset = financialInfo.getCurrentAssets();
        Integer monthlyLivingCost = financialInfo.getMonthlyFixedExpense();
        Integer cashGapDay = calculateCashGapDay(currentAsset, monthlyLivingCost);
        Integer approvalExpectedDays = prediction.getPredictionMedianDays();
        Integer paymentExpectedDays = approvalExpectedDays + 14;

        return new StrategyContext(
                regionCode,
                regionName,
                injuryName,
                accidentInfo.getJob().getJobName(),
                accidentInfo.getEmploymentType().name(),
                currentAsset,
                monthlyLivingCost,
                cashGapDay,
                approvalExpectedDays,
                paymentExpectedDays
        );
    }

    private Integer calculateCashGapDay(Integer currentAsset, Integer monthlyLivingCost) {
        if (monthlyLivingCost == null || monthlyLivingCost == 0) {
            return 1;
        }

        double dailyLivingCost = monthlyLivingCost / 30.0;
        int days = (int) Math.ceil(zeroIfNull(currentAsset) / dailyLivingCost);

        return Math.max(days, 1);
    }

    private StrategyResult getLatestResult(Long userId) {
        return strategyResultRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.STRATEGY_NOT_FOUND));
    }

    private List<StrategyItem> toStrategyItems(
            Long strategyResultId,
            AiStrategyResult aiResult,
            List<CandidateSupportDto> candidates
    ) {
        if (aiResult.strategies() == null || aiResult.strategies().isEmpty()) {
            throw new CustomException(ErrorCode.AI_RESPONSE_PARSE_FAILED);
        }

        return aiResult.strategies().stream()
                .flatMap(plan -> {
                    StrategyType strategyType = parseStrategyType(plan.strategyType());

                    if (plan.items() == null) {
                        return List.<StrategyItem>of().stream();
                    }

                    return plan.items().stream()
                            .map(item -> {
                                CandidateSupportDto candidate = findCandidate(
                                        candidates,
                                        item.externalId(),
                                        item.itemName()
                                );

                                StrategyItemType itemType = parseItemType(item.itemType());
                                int receiveDay = item.expectedReceiveDay() == null ? 30 : item.expectedReceiveDay();

                                return StrategyItem.builder()
                                        .strategyResultId(strategyResultId)
                                        .strategyType(strategyType)
                                        .itemType(itemType)
                                        .externalId(item.externalId())
                                        .itemName(item.itemName())
                                        .itemDescription(candidate == null ? null : candidate.description())
                                        .expectedAmount(zeroIfNull(item.expectedAmount()))
                                        .repaymentRequired(Boolean.TRUE.equals(item.repaymentRequired()))
                                        .overlapsWithWorkersCompensation(Boolean.TRUE.equals(item.overlapsWithWorkersCompensation()))
                                        .applyUrl(candidate == null ? null : candidate.applyUrl())
                                        .expectedApplyDate(LocalDateTime.now())
                                        .expectedReceiveDate(LocalDateTime.now().plusDays(receiveDay))
                                        .aiReason(item.aiReason())
                                        .build();
                            });
                })
                .toList();
    }

    private StrategyType parseStrategyType(String strategyType) {
        try {
            return StrategyType.valueOf(strategyType);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_STRATEGY_TYPE);
        }
    }

    private StrategyItemType parseItemType(String itemType) {
        try {
            return StrategyItemType.valueOf(itemType);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_RESPONSE_PARSE_FAILED);
        }
    }

    private CandidateSupportDto findCandidate(
            List<CandidateSupportDto> candidates,
            String externalId,
            String itemName
    ) {
        return candidates.stream()
                .filter(candidate ->
                        externalId != null && externalId.equals(candidate.externalId())
                                || itemName != null && itemName.equals(candidate.name())
                )
                .findFirst()
                .orElse(null);
    }


    private List<StrategyCardDto> toStrategyCards(List<StrategyItem> items) {
        return List.of(
                toStrategyCard(
                        StrategyType.STRATEGY_1,
                        "전략 1",
                        "최대한 빠르게 받을 수 있는 전략이에요.",
                        items
                ),
                toStrategyCard(
                        StrategyType.STRATEGY_2,
                        "전략 2",
                        "최대한 많이 받을 수 있는 전략이에요.",
                        items
                )
        );
    }

    private StrategyCardDto toStrategyCard(
            StrategyType strategyType,
            String title,
            String summary,
            List<StrategyItem> allItems
    ) {
        List<StrategyItem> filtered = allItems.stream()
                .filter(item -> item.getStrategyType() == strategyType)
                .toList();

        List<StrategyItemDto> supports = filtered.stream()
                .filter(item -> item.getItemType() != StrategyItemType.MICRO_FINANCE_LOAN)
                .map(this::toStrategyItemDto)
                .toList();

        List<StrategyItemDto> loans = filtered.stream()
                .filter(item -> item.getItemType() == StrategyItemType.MICRO_FINANCE_LOAN)
                .map(this::toStrategyItemDto)
                .toList();

        int total = filtered.stream()
                .mapToInt(item -> zeroIfNull(item.getExpectedAmount()))
                .sum();

        return new StrategyCardDto(
                strategyType,
                title,
                summary,
                total,
                supports,
                loans
        );
    }

    private StrategyItemDto toStrategyItemDto(StrategyItem item) {
        return new StrategyItemDto(
                item.getId(),
                item.getItemType(),
                item.getItemName(),
                item.getItemDescription(),
                item.getExpectedAmount(),
                item.getRepaymentRequired(),
                item.getOverlapsWithWorkersCompensation(),
                item.getApplyUrl(),
                item.getExpectedApplyDate(),
                item.getExpectedReceiveDate(),
                item.getAiReason()
        );
    }

    private List<CashflowPointDto> toCashflowDtos(List<CashflowSnapshot> snapshots) {
        return snapshots.stream()
                .sorted(Comparator.comparing(CashflowSnapshot::getDayOffset).thenComparing(CashflowSnapshot::getId))
                .map(snapshot -> new CashflowPointDto(
                        snapshot.getId(),
                        snapshot.getStrategyType(),
                        snapshot.getSnapshotDate(),
                        snapshot.getDayOffset(),
                        snapshot.getIncome(),
                        snapshot.getExpense(),
                        snapshot.getBalance(),
                        snapshot.getEventType(),
                        snapshot.getEventMemo()
                ))
                .toList();
    }

    private List<TimelineItemDto> toTimelineDtos(List<CashflowSnapshot> snapshots) {
        return snapshots.stream()
                .filter(snapshot -> snapshot.getEventType() != CashflowEventType.CURRENT_ASSET)
                .sorted(Comparator.comparing(CashflowSnapshot::getDayOffset).thenComparing(CashflowSnapshot::getId))
                .map(snapshot -> new TimelineItemDto(
                        snapshot.getSnapshotDate(),
                        snapshot.getDayOffset(),
                        snapshot.getEventType(),
                        snapshot.getEventMemo(),
                        snapshot.getIncome() > 0 ? snapshot.getIncome() : -snapshot.getExpense(),
                        snapshot.getBalance()
                ))
                .toList();
    }

    private int zeroIfNull(Integer value) {
        return value == null ? 0 : value;
    }

    private boolean isEmpty(List<Long> values) {
        return values == null || values.isEmpty();
    }

    private List<Long> nullToEmpty(List<Long> values) {
        return values == null ? List.of() : values;
    }
}