package com.example.butim.domain.prediction.client;

import com.example.butim.domain.prediction.config.PrecedentDataProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrecedentApiClient {

    private final PrecedentDataProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 유형별 판례 건수 조회
     * @param kindA 사건결과 (승인/기각)
     * @param kindB 사건유형 (요양/장해/유족 등)
     * @param kindC 사고·질병 구분 (업무상사고/업무상질병)
     */
    public JsonNode fetchCaseCount(String kindA, String kindB, String kindC) {
        return fetch(properties.getCountPath(), kindA, kindB, kindC, properties.getPageNo(), 1);
    }

    /**
     * 판결문 내용 조회 (GPT 컨텍스트용)
     * @param kindB 사건유형 (요양)
     * @param kindC 사고·질병 구분
     */
    public JsonNode fetchCaseContents(String kindB, String kindC) {
        return fetch(properties.getContentPath(), null, kindB, kindC,
                properties.getPageNo(), properties.getNumOfRows());
    }

    private JsonNode fetch(String path, String kindA, String kindB, String kindC, int pageNo, int numOfRows) {
        RestClient restClient = RestClient.create();

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(properties.getBaseUrl() + path)
                .queryParam("serviceKey", properties.getServiceKey())
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows);

        if (kindA != null) builder.queryParam("kindA", kindA);
        if (kindB != null) builder.queryParam("kindB", kindB);
        if (kindC != null) builder.queryParam("kindC", kindC);

        URI uri = builder.build().toUri();
        log.info("판례 API 요청: {}", uri);

        try {
            String responseBody = restClient.get()
                    .uri(uri)
                    .accept(MediaType.ALL)
                    .retrieve()
                    .body(String.class);

            return objectMapper.readTree(responseBody);
        } catch (Exception e) {
            log.error("판례 API 호출 실패 - path: {}, error: {}", path, e.getMessage());
            throw new RuntimeException("판례 API 호출 중 오류가 발생했습니다.", e);
        }
    }
}
