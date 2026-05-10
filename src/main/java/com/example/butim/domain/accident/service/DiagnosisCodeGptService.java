package com.example.butim.domain.accident.service;

import com.example.butim.domain.accident.dto.response.DiagnosisCodeResponse;
import com.example.butim.domain.accident.repository.DiagnosisCodeRepository;
import com.example.butim.external.openai.GptClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnosisCodeGptService {

    private final GptClient gptClient;
    private final DiagnosisCodeRepository diagnosisCodeRepository;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
            당신은 산업재해 의료 전문가입니다.
            사용자가 입력한 부상 설명에서 다음 두 가지를 추출하세요.
            1. bodyPart: 다친 신체 부위 (예: 요추, 손가락, 무릎, 어깨)
            2. injuryType: 부상 유형 (예: 골절, 절단, 파열, 염좌, 진폐)
            반드시 아래 JSON 형식으로만 응답하세요. 다른 텍스트는 절대 포함하지 마세요.
            {"bodyPart": "...", "injuryType": "..."}
            """;

    private record GptKeyword(String bodyPart, String injuryType) {}

    @Transactional(readOnly = true)
    public List<DiagnosisCodeResponse> suggest(String userInput) {
        try {
            String content = gptClient.chat(SYSTEM_PROMPT, userInput);
            GptKeyword keyword = objectMapper.readValue(content, GptKeyword.class);

            return diagnosisCodeRepository
                    .findTop3ByKeywords(keyword.bodyPart(), keyword.injuryType())
                    .stream()
                    .map(DiagnosisCodeResponse::from)
                    .toList();

        } catch (Exception e) {
            log.error("주상병코드 추천 실패 - input: {}, error: {}", userInput, e.getMessage());
            return List.of();
        }
    }
}
