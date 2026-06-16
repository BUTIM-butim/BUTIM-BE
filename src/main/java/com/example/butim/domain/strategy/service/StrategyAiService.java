package com.example.butim.domain.strategy.service;

import com.example.butim.domain.strategy.dto.common.AiStrategyResult;
import com.example.butim.domain.strategy.dto.common.CandidateSupportDto;
import com.example.butim.domain.strategy.dto.common.StrategyContext;
import com.example.butim.domain.strategy.openai.OpenAiClient;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StrategyAiService {

    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    public AiStrategyResult recommend(
            List<CandidateSupportDto> candidates,
            StrategyContext context
    ) {
        String systemPrompt = """
                너는 산재 승인 대기 중인 사용자를 위한 현금흐름 대응 전략 추천 엔진이다.
                반드시 JSON만 반환한다.
                
                규칙:
                1. 전략은 정확히 2개를 만든다.
                2. STRATEGY_1은 가장 빠르게 현금 공백을 줄이는 전략이다.
                3. STRATEGY_2는 예상 수령 금액을 최대화하는 전략이다.
                4. 후보 목록에 없는 지원금이나 대출은 절대 만들지 않는다.
                5. 대출 상품은 itemType을 MICRO_FINANCE_LOAN으로 둔다.
                6. 대출 상품은 repaymentRequired를 true로 둔다.
                7. 복지서비스(itemType이 CENTRAL_WELFARE 또는 LOCAL_WELFARE)는 repaymentRequired를 false로 둔다.
                8. 산재보험과 중복 가능성이 있어 보이면 overlapsWithWorkersCompensation을 true로 둔다.
                9. expectedReceiveDay는 오늘 기준 며칠 뒤 받을 수 있는지를 의미하는 숫자다.
                10. 전략별 items는 2개에서 4개 사이로 구성한다.
                11. 지원금(복지서비스)은 상환 의무가 없는 항목이므로 두 전략 모두 반드시 포함해야 한다. 후보 목록에 복지서비스가 2개 이상 있다면 1개만 넣지 말고, 전략별 items 개수 범위(2~4개) 안에서 사용자에게 적합한 복지서비스를 가능한 한 여러 개 포함한다. 후보 목록에 복지서비스가 전혀 없을 때만 예외로 한다.
                12. 두 전략에 포함하는 복지서비스 구성은 서로 달라야 한다. 후보 목록에 복지서비스가 2개 이상 있다면, STRATEGY_1과 STRATEGY_2에 완전히 동일한 복지서비스 목록을 넣지 말고 각 전략의 목적에 맞는 다른 후보를 선택한다.
                13. 저금리 대출(MICRO_FINANCE_LOAN)은 상환 의무가 있는 선택 항목으로, 두 전략을 구분하는 기준이 된다. STRATEGY_1(가장 빠르게 현금 공백을 줄이는 전략)은 상환 부담을 늘리지 않기 위해 대출을 포함하지 않는 것을 우선한다. STRATEGY_2(수령 금액을 최대화하는 전략)는 필요하다면 후보 목록의 저금리 대출을 포함해 총 수령액을 늘린다. 후보 목록에 대출이 없다면 두 전략 모두 대출 없이 구성한다.
                
                반환 JSON 형식:
                {
                  "summary": "전체 추천 요약",
                  "strategies": [
                    {
                      "strategyType": "STRATEGY_1",
                      "title": "전략 1",
                      "summary": "전략 설명",
                      "items": [
                        {
                          "externalId": "후보 externalId",
                          "itemName": "지원명",
                          "itemType": "CENTRAL_WELFARE 또는 LOCAL_WELFARE 또는 MICRO_FINANCE_LOAN",
                          "expectedAmount": 400000,
                          "repaymentRequired": false,
                          "overlapsWithWorkersCompensation": false,
                          "expectedReceiveDay": 60,
                          "aiReason": "추천 이유"
                        }
                      ]
                    },
                    {
                      "strategyType": "STRATEGY_2",
                      "title": "전략 2",
                      "summary": "전략 설명",
                      "items": []
                    }
                  ]
                }
                """;

        String userPrompt = """
                사용자 정보:
                - 지역 코드: %s
                - 지역명: %s
                - 상병명: %s
                - 직종: %s
                - 고용형태: %s
                - 현재 자산: %d원
                - 월 생활비: %d원
                - 현금 공백 발생일: D-%d
                - 산재 승인 예상일: D-%d
                - 산재 지급 예상일: D-%d

                후보 지원금/대출 목록:
                %s
                """.formatted(
                value(context.regionCode()),
                value(context.regionName()),
                value(context.injuryName()),
                value(context.jobType()),
                value(context.employmentType()),
                context.currentAsset(),
                context.monthlyLivingCost(),
                context.cashGapDay(),
                context.approvalExpectedDays(),
                context.paymentExpectedDays(),
                toJson(candidates)
        );

        String json = openAiClient.createStrategyJson(systemPrompt, userPrompt);

        try {
            return objectMapper.readValue(json, AiStrategyResult.class);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_RESPONSE_PARSE_FAILED);
        }
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            return "[]";
        }
    }

    private String value(Object value) {
        return value == null ? "없음" : String.valueOf(value);
    }
}