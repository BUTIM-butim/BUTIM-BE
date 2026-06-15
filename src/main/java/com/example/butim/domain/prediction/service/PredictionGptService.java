package com.example.butim.domain.prediction.service;

import com.example.butim.domain.accident.entity.AccidentInfo;
import com.example.butim.domain.prediction.stats.*;
import com.example.butim.external.openai.GptClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionGptService {

    private final GptClient gptClient;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
            당신은 대한민국 산업재해 요양 기간 예측 전문가입니다.
            주어진 통계 데이터와 판례 정보를 분석하여 해당 산재 사례의 요양 기간을 예측합니다.
            반드시 JSON 형식으로만 응답하십시오.
            응답 형식:
            {
              "minDays": 최소 예상 요양일수(정수),
              "maxDays": 최대 예상 요양일수(정수),
              "medianDays": 중간 예상 요양일수(정수),
              "analysisText": "예측 근거 요약 (200자 이내)"
            }
            """;

    public record GptPredictionResult(int minDays, int maxDays, int medianDays, String analysisText) {}

    public GptPredictionResult predict(AccidentInfo accidentInfo, DiagnosisStats diagnosisStats,
                                       ApprovalStats approvalStats, CorrectionFactor correctionFactor,
                                       PrecedentStats precedentStats) {
        String userMessage = buildUserMessage(accidentInfo, diagnosisStats, approvalStats,
                correctionFactor, precedentStats);

        log.info("GPT 예측 요청 시작");
        String response = gptClient.chat(SYSTEM_PROMPT, userMessage, true);

        return parseResponse(response);
    }

    private String buildUserMessage(AccidentInfo accidentInfo, DiagnosisStats diagnosisStats,
                                    ApprovalStats approvalStats, CorrectionFactor correctionFactor,
                                    PrecedentStats precedentStats) {
        StringBuilder sb = new StringBuilder();

        sb.append("## 사용자 산재 정보\n");
        sb.append("- 나이: ").append(accidentInfo.getAge()).append("세\n");
        sb.append("- 성별: ").append(accidentInfo.getGender().getLabel()).append("\n");
        sb.append("- 업종: ").append(accidentInfo.getIndustry().getIndustryName()).append("\n");
        sb.append("- 직종: ").append(accidentInfo.getJob().getJobName()).append("\n");
        sb.append("- 사업장 규모: ").append(accidentInfo.getBusinessSize().getLabel()).append("\n");
        sb.append("- 고용형태: ").append(accidentInfo.getEmploymentType().getLabel()).append("\n");

        if (accidentInfo.getDiagnosisCode() != null) {
            sb.append("- 주상병코드: ").append(accidentInfo.getDiagnosisCode().getCode())
              .append(" (").append(accidentInfo.getDiagnosisCode().getName()).append(")\n");
        } else {
            sb.append("- 주상병코드: 미입력\n");
        }

        if (accidentInfo.getAdditionalInfo() != null) {
            sb.append("- 추가 정보: ").append(accidentInfo.getAdditionalInfo()).append("\n");
        }

        sb.append("\n## 유사 사례 요양일수 통계\n");
        if (diagnosisStats.similarCaseCount() > 0) {
            sb.append("- 유사 사례 수: ").append(diagnosisStats.similarCaseCount()).append("건\n");
            sb.append("- 최소 요양일수: ").append(diagnosisStats.minDays()).append("일\n");
            sb.append("- 최대 요양일수: ").append(diagnosisStats.maxDays()).append("일\n");
            sb.append("- 중앙값 요양일수: ").append(diagnosisStats.medianDays()).append("일\n");
        } else {
            sb.append("- 해당 주상병 데이터 없음\n");
        }

        sb.append("\n## 업종·직종별 승인율\n");
        sb.append(String.format("- 업종 승인율: %.1f%%\n", approvalStats.industryApprovalRate() * 100));
        sb.append(String.format("- 직종 승인율: %.1f%%\n", approvalStats.jobApprovalRate() * 100));

        sb.append("\n## 보정계수\n");
        sb.append(String.format("- 사업장규모 보정: %.2f\n", correctionFactor.businessSizeMultiplier()));
        sb.append(String.format("- 연령 보정: %.2f\n", correctionFactor.ageMultiplier()));

        sb.append("\n## 판례 통계\n");
        sb.append("- 승인 판례: ").append(precedentStats.approvedCount()).append("건\n");
        sb.append("- 기각 판례: ").append(precedentStats.rejectedCount()).append("건\n");
        if (precedentStats.totalCount() > 0) {
            sb.append(String.format("- 판례 기반 승인율: %.1f%%\n", precedentStats.approvalRate() * 100));
        }

        List<String> contents = precedentStats.caseContents();
        if (!contents.isEmpty()) {
            sb.append("\n## 유사 판례 요약 (최대 3건)\n");
            contents.stream().limit(3).forEach(c ->
                    sb.append("- ").append(c, 0, Math.min(c.length(), 200)).append("\n")
            );
        }

        return sb.toString();
    }

    private GptPredictionResult parseResponse(String response) {
        try {
            JsonNode node = objectMapper.readTree(response);
            return new GptPredictionResult(
                    node.path("minDays").asInt(),
                    node.path("maxDays").asInt(),
                    node.path("medianDays").asInt(),
                    node.path("analysisText").asText()
            );
        } catch (Exception e) {
            log.error("GPT 응답 파싱 실패: {}", response, e);
            throw new RuntimeException("GPT 예측 응답을 파싱하지 못했습니다.", e);
        }
    }
}
