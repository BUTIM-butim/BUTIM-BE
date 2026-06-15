package com.example.butim.domain.prediction.client;

import com.example.butim.domain.prediction.config.PredictionDataProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
public class PredictionDataClient {

    private final PredictionDataProperties properties;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public JsonNode fetchIndustryApplyData() {
        return fetchData(properties.getIndustryApplyPath());
    }

    public JsonNode fetchIndustryApprovalData() {
        return fetchData(properties.getIndustryApprovalPath());
    }

    public JsonNode fetchJobApplyData() {
        return fetchData(properties.getJobApplyPath());
    }

    public JsonNode fetchJobApprovalData() {
        return fetchData(properties.getJobApprovalPath());
    }

    public JsonNode fetchBusinessSizeApplyData() {
        return fetchData(properties.getBusinessSizeApplyPath());
    }

    public JsonNode fetchBusinessSizeApprovalData() {
        return fetchData(properties.getBusinessSizeApprovalPath());
    }

    public JsonNode fetchAgeApplyData() {
        return fetchData(properties.getAgeApplyPath());
    }

    public JsonNode fetchAgeApprovalData() {
        return fetchData(properties.getAgeApprovalPath());
    }

    public JsonNode fetchAccidentTypeApprovalData() {
        return fetchData(properties.getAccidentTypeApprovalPath());
    }

    public JsonNode fetchDiagnosisApplyData() {
        return fetchData(properties.getDiagnosisApplyPath());
    }

    public JsonNode fetchDiagnosisApprovalData() {
        return fetchData(properties.getDiagnosisApprovalPath());
    }

    private JsonNode fetchData(String path) {
        RestClient restClient = RestClient.create();

        URI uri = UriComponentsBuilder
                .fromUriString(properties.getBaseUrl() + path)
                .queryParam("serviceKey", properties.getServiceKey())
                .queryParam("pageNo", properties.getPageNo())
                .queryParam("numOfRows", properties.getNumOfRows())
                .queryParam("_type", "json")
                .build()
                .toUri();

        log.info("산재 예측 데이터 API 요청 URL: {}", uri);

        try {
            String responseBody = restClient.get()
                    .uri(uri)
                    .accept(MediaType.ALL)
                    .retrieve()
                    .body(String.class);

            return parseResponse(responseBody);

        } catch (Exception e) {
            log.error("산재 예측 데이터 API 호출 실패 - path: {}, error: {}", path, e.getMessage());
            throw new RuntimeException("산재 예측 데이터 API 호출 중 오류가 발생했습니다.", e);
        }
    }

    private JsonNode parseResponse(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            throw new RuntimeException("산재 예측 데이터 API 응답이 비어 있습니다.");
        }

        String trimmed = responseBody.trim();

        try {
            if (trimmed.startsWith("<")) {
                return xmlMapper.readTree(trimmed);
            }
            return objectMapper.readTree(trimmed);
        } catch (Exception e) {
            log.error("산재 예측 데이터 API 응답 파싱 실패", e);
            throw new RuntimeException("산재 예측 데이터 API 응답을 파싱하지 못했습니다.");
        }
    }
}
