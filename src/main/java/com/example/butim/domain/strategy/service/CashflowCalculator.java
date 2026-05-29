package com.example.butim.domain.strategy.service;

import com.example.butim.domain.strategy.entity.CashflowSnapshot;
import com.example.butim.domain.strategy.entity.StrategyItem;
import com.example.butim.domain.strategy.entity.StrategyResult;
import com.example.butim.domain.strategy.enums.CashflowEventType;
import com.example.butim.domain.strategy.enums.StrategyItemType;
import com.example.butim.domain.strategy.enums.StrategyType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class CashflowCalculator {

    public List<CashflowSnapshot> createInitialSnapshots(
            StrategyResult result,
            List<StrategyItem> items
    ) {
        List<CashflowSnapshot> snapshots = new ArrayList<>();

        snapshots.addAll(createSnapshotsByStrategy(result, StrategyType.STRATEGY_1, items));
        snapshots.addAll(createSnapshotsByStrategy(result, StrategyType.STRATEGY_2, items));

        return snapshots;
    }

    public List<CashflowSnapshot> recalculateSelectedStrategy(
            StrategyResult result,
            List<StrategyItem> selectedItems,
            List<Long> appliedItemIds,
            List<Long> receivedItemIds
    ) {
        return createSnapshots(
                result,
                result.getSelectedStrategyType(),
                selectedItems,
                appliedItemIds == null ? List.of() : appliedItemIds,
                receivedItemIds == null ? List.of() : receivedItemIds
        );
    }

    private List<CashflowSnapshot> createSnapshotsByStrategy(
            StrategyResult result,
            StrategyType strategyType,
            List<StrategyItem> items
    ) {
        List<StrategyItem> selectedItems = items.stream()
                .filter(item -> item.getStrategyType() == strategyType)
                .toList();

        return createSnapshots(result, strategyType, selectedItems, List.of(), List.of());
    }

    private List<CashflowSnapshot> createSnapshots(
            StrategyResult result,
            StrategyType strategyType,
            List<StrategyItem> items,
            List<Long> appliedItemIds,
            List<Long> receivedItemIds
    ) {
        List<CashflowEvent> events = new ArrayList<>();

        events.add(new CashflowEvent(
                0,
                0,
                0,
                CashflowEventType.CURRENT_ASSET,
                "전체 자산"
        ));

        events.add(new CashflowEvent(
                result.getCashGapDay(),
                0,
                livingCostUntil(result, result.getCashGapDay()),
                CashflowEventType.CASH_GAP,
                "현금 공백 발생"
        ));

        if (result.getHospitalCost() != null && result.getHospitalCost() > 0) {
            events.add(new CashflowEvent(
                    1,
                    0,
                    result.getHospitalCost(),
                    CashflowEventType.HOSPITAL_COST,
                    "병원비"
            ));
        }

        if (result.getInsuranceAmount() != null && result.getInsuranceAmount() > 0) {
            events.add(new CashflowEvent(
                    1,
                    result.getInsuranceAmount(),
                    0,
                    CashflowEventType.INSURANCE_RECEIVED,
                    "보험금"
            ));
        }

        for (StrategyItem item : items) {
            boolean isReceived = receivedItemIds.contains(item.getId());
            boolean isApplied = appliedItemIds.contains(item.getId());

            if (isReceived) {
                events.add(new CashflowEvent(
                        1,
                        safe(item.getExpectedAmount()),
                        0,
                        eventTypeForReceived(item),
                        item.getItemName()
                ));
            } else if (isApplied) {
                events.add(new CashflowEvent(
                        1,
                        0,
                        0,
                        eventTypeForApplied(item),
                        item.getItemName() + " 신청"
                ));
            } else {
                events.add(new CashflowEvent(
                        estimateReceiveDay(item),
                        safe(item.getExpectedAmount()),
                        0,
                        eventTypeForReceived(item),
                        item.getItemName()
                ));
            }
        }

        if (result.getExpectedWorkersCompensationAmount() != null
                && result.getExpectedWorkersCompensationAmount() > 0) {
            events.add(new CashflowEvent(
                    result.getPaymentExpectedDays(),
                    result.getExpectedWorkersCompensationAmount(),
                    0,
                    CashflowEventType.WORKERS_COMPENSATION_RECEIVED,
                    "산재보험 예상 지급"
            ));
        }

        return buildSnapshots(result, strategyType, events);
    }

    private List<CashflowSnapshot> buildSnapshots(
            StrategyResult result,
            StrategyType strategyType,
            List<CashflowEvent> events
    ) {
        LocalDate baseDate = LocalDate.now();

        List<CashflowEvent> sortedEvents = events.stream()
                .sorted(Comparator.comparing(CashflowEvent::dayOffset))
                .toList();

        List<CashflowSnapshot> snapshots = new ArrayList<>();
        int balance = result.getCurrentAsset();

        for (CashflowEvent event : sortedEvents) {
            if (event.eventType() == CashflowEventType.CURRENT_ASSET) {
                balance = result.getCurrentAsset();
            } else if (event.eventType() == CashflowEventType.CASH_GAP) {
                balance = result.getCurrentAsset() - event.expense();
            } else {
                int livingCost = livingCostUntil(result, event.dayOffset());
                balance = result.getCurrentAsset() - livingCost + event.income() - event.expense();
            }

            snapshots.add(CashflowSnapshot.builder()
                    .strategyResultId(result.getId())
                    .strategyType(strategyType)
                    .snapshotDate(baseDate.plusDays(event.dayOffset()))
                    .dayOffset(event.dayOffset())
                    .income(event.income())
                    .expense(event.expense())
                    .balance(balance)
                    .eventType(event.eventType())
                    .eventMemo(event.memo())
                    .build());
        }

        return snapshots;
    }

    private int livingCostUntil(StrategyResult result, int dayOffset) {
        return (result.getMonthlyLivingCost() / 30) * dayOffset;
    }

    private int estimateReceiveDay(StrategyItem item) {
        if (item.getExpectedReceiveDate() == null) {
            return 30;
        }

        long days = java.time.Duration.between(
                java.time.LocalDateTime.now(),
                item.getExpectedReceiveDate()
        ).toDays();

        return Math.max((int) days, 1);
    }

    private CashflowEventType eventTypeForReceived(StrategyItem item) {
        if (item.getItemType() == StrategyItemType.MICRO_FINANCE_LOAN) {
            return CashflowEventType.LOAN_RECEIVED;
        }

        return CashflowEventType.WELFARE_RECEIVED;
    }

    private CashflowEventType eventTypeForApplied(StrategyItem item) {
        if (item.getItemType() == StrategyItemType.MICRO_FINANCE_LOAN) {
            return CashflowEventType.LOAN_APPLIED;
        }

        return CashflowEventType.WELFARE_APPLIED;
    }

    private int safe(Integer amount) {
        return amount == null ? 0 : amount;
    }

    private record CashflowEvent(
            Integer dayOffset,
            Integer income,
            Integer expense,
            CashflowEventType eventType,
            String memo
    ) {
    }
}