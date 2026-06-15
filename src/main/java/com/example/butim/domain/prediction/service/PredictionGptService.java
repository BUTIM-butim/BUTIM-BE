package com.example.butim.domain.prediction.service;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.prediction.stats.DiagnosisStats;
import com.example.butim.external.openai.GptClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionGptService {

    private final GptClient gptClient;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
            당신은 대한민국 산업재해 승인 처리 기간 예측 전문가입니다.
            근로복지공단의 산재 승인까지 소요되는 기간을 예측합니다.

            - 업종별 산재 승인율은 해당 업종에서 산재가 얼마나 잘 인정되는지 나타내는 지표입니다. 승인율이 높을수록 처리가 수월합니다.
            - 주상병별 평균 요양일수는 부상의 심각도와 복잡도를 나타내는 지표입니다. 복잡할수록 심사 기간이 길어질 수 있습니다.
            - 한국 근로복지공단의 일반적인 산재 승인 처리 기간은 단순 사고의 경우 14~30일, 복잡한 사례는 60~180일 이상 소요될 수 있습니다.

            반드시 아래 JSON 형식으로만 응답하십시오.
            {
              "minDays": 최소 예상 승인 소요일수(정수),
              "maxDays": 최대 예상 승인 소요일수(정수),
              "medianDays": 평균 예상 승인 소요일수(정수),
              "reliabilityLevel": "HIGH" 또는 "MEDIUM" 또는 "LOW",
              "reliabilityScore": 예측 신뢰도 0~100 정수,
              "similarCaseCount": 유사 사례 추정 건수(정수),
              "analysisReasons": ["판단 근거 1문장", "판단 근거 2문장", "판단 근거 3문장"]
            }
            """;

    public record GptPredictionResult(
            int minDays,
            int maxDays,
            int medianDays,
            String reliabilityLevel,
            int reliabilityScore,
            int similarCaseCount,
            List<String> analysisReasons
    ) {}

    public GptPredictionResult predict(AccidentInfo accidentInfo, double industryApprovalRate,
                                       DiagnosisStats diagnosisStats) {
        String userMessage = buildUserMessage(accidentInfo, industryApprovalRate, diagnosisStats);
        log.info("GPT 산재 승인 기간 예측 요청 시작");
        String response = gptClient.chat(SYSTEM_PROMPT, userMessage, true);
        return parseResponse(response);
    }

    private String buildUserMessage(AccidentInfo accidentInfo, double industryApprovalRate,
                                    DiagnosisStats diagnosisStats) {
        StringBuilder sb = new StringBuilder();

        sb.append("## 산재 신청자 정보\n");
        sb.append("- 나이: ").append(accidentInfo.getAge()).append("세\n");
        sb.append("- 성별: ").append(accidentInfo.getGender().getLabel()).append("\n");
        sb.append("- 업종: ").append(accidentInfo.getIndustry().getIndustryName());
        if (industryApprovalRate > 0) {
            sb.append(String.format(" (해당 업종 산재 승인율: %.1f%%)", industryApprovalRate * 100));
        }
        sb.append("\n");
        sb.append("- 직종: ").append(accidentInfo.getJob().getJobName()).append("\n");
        sb.append("- 사업장 규모: ").append(accidentInfo.getBusinessSize().getLabel()).append("\n");
        sb.append("- 고용형태: ").append(accidentInfo.getEmploymentType().getLabel()).append("\n");

        if (accidentInfo.getDiagnosisCode() != null) {
            sb.append("- 주상병: ").append(accidentInfo.getDiagnosisCode().getCode())
              .append(" ").append(accidentInfo.getDiagnosisCode().getName()).append("\n");
        } else {
            sb.append("- 주상병: 미입력\n");
        }

        if (diagnosisStats.medianDays() > 0) {
            sb.append("\n## 부상 복잡도 지표 (주상병별 요양일수 통계)\n");
            sb.append("- 해당 부상 유형 평균 요양일수: ").append(diagnosisStats.medianDays()).append("일\n");
            sb.append("- 요양일수 범위: ").append(diagnosisStats.minDays())
              .append("~").append(diagnosisStats.maxDays()).append("일\n");
        }

        if (accidentInfo.getAdditionalInfo() != null && !accidentInfo.getAdditionalInfo().isBlank()) {
            sb.append("\n## 추가 사고 정보\n");
            sb.append(accidentInfo.getAdditionalInfo()).append("\n");
        }

        return sb.toString();
    }

    private GptPredictionResult parseResponse(String response) {
        try {
            JsonNode node = objectMapper.readTree(response);

            List<String> reasons = new ArrayList<>();
            JsonNode reasonsNode = node.path("analysisReasons");
            if (reasonsNode.isArray()) {
                reasonsNode.forEach(r -> reasons.add(r.asText()));
            }

            return new GptPredictionResult(
                    node.path("minDays").asInt(),
                    node.path("maxDays").asInt(),
                    node.path("medianDays").asInt(),
                    node.path("reliabilityLevel").asText("MEDIUM"),
                    node.path("reliabilityScore").asInt(50),
                    node.path("similarCaseCount").asInt(0),
                    reasons
            );
        } catch (Exception e) {
            log.error("GPT 응답 파싱 실패: {}", response, e);
            throw new RuntimeException("GPT 예측 응답을 파싱하지 못했습니다.", e);
        }
    }
}
