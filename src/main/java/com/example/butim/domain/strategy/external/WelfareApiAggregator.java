package com.example.butim.domain.strategy.external;

import com.example.butim.domain.strategy.dto.common.CandidateSupportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class WelfareApiAggregator {

    private final CentralWelfareApiClient centralWelfareApiClient;
    private final LocalWelfareApiClient localWelfareApiClient;
    private final MicroFinanceApiClient microFinanceApiClient;

    public List<CandidateSupportDto> collectCandidates(String regionName) {
        List<CandidateSupportDto> candidates = new ArrayList<>();

        safeAdd(candidates, "중앙부처복지서비스-의료", () -> centralWelfareApiClient.search("의료"));
        safeAdd(candidates, "중앙부처복지서비스-생계", () -> centralWelfareApiClient.search("생계"));
        safeAdd(candidates, "중앙부처복지서비스-긴급", () -> centralWelfareApiClient.search("긴급"));
        safeAdd(candidates, "중앙부처복지서비스-전기", () -> centralWelfareApiClient.search("전기"));

        safeAdd(candidates, "지자체복지서비스-의료", () -> localWelfareApiClient.search(regionName, "의료"));
        safeAdd(candidates, "지자체복지서비스-생계", () -> localWelfareApiClient.search(regionName, "생계"));
        safeAdd(candidates, "지자체복지서비스-긴급", () -> localWelfareApiClient.search(regionName, "긴급"));

        safeAdd(candidates, "서민금융진흥원-대출상품", microFinanceApiClient::search);

        log.info("공공데이터 후보 조회 결과 수: {}", candidates.size());

        return candidates.stream()
                .filter(item -> item.name() != null)
                .filter(distinctByName())
                .sorted(Comparator.comparing(
                        CandidateSupportDto::expectedAmount,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .limit(50)
                .toList();
    }

    private void safeAdd(
            List<CandidateSupportDto> target,
            String apiName,
            Supplier<List<CandidateSupportDto>> supplier
    ) {
        try {
            List<CandidateSupportDto> result = supplier.get();

            if (result == null || result.isEmpty()) {
                log.warn("{} 조회 결과 없음", apiName);
                return;
            }

            log.info("{} 조회 성공: {}건", apiName, result.size());
            target.addAll(result);

        } catch (Exception e) {
            log.error("{} 조회 실패", apiName, e);
        }
    }

    private java.util.function.Predicate<CandidateSupportDto> distinctByName() {
        List<String> names = new ArrayList<>();

        return item -> {
            String name = item.name();

            if (Objects.isNull(name) || names.contains(name)) {
                return false;
            }

            names.add(name);
            return true;
        };
    }
}