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
                7. 복지서비스는 repaymentRequired를 false로 둔다.
                8. 산재보험과 중복 가능성이 있어 보이면 overlapsWithWorkersCompensation을 true로 둔다.
                9. expectedReceiveDay는 오늘 기준 며칠 뒤 받을 수 있는지를 의미하는 숫자다.
                10. 전략별 items는 2개에서 4개 사이로 구성한다.
                
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