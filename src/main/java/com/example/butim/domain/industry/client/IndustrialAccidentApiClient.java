package com.example.butim.domain.industry.client;

import com.example.butim.domain.industry.config.IndustrialAccidentProperties;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
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
public class IndustrialAccidentApiClient {

    private final IndustrialAccidentProperties properties;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public JsonNode fetchIndustryData() {
        return fetchData(properties.getIndustryPath());
    }

    public JsonNode fetchJobData() {
        return fetchData(properties.getJobPath());
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

        log.info("공공데이터 API 요청 URL: {}", uri);

        String responseBody;
        try {
            responseBody = restClient.get()
                    .uri(uri)
                    .accept(MediaType.ALL)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.error("공공데이터 API 요청 실패. url={}", uri, e);
            throw new CustomException(ErrorCode.PUBLIC_DATA_REQUEST_FAILED);
        }

        log.info("공공데이터 API 응답 길이: {}", responseBody != null ? responseBody.length() : 0);

        return parseResponse(responseBody);
    }

    private JsonNode parseResponse(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            throw new RuntimeException("공공데이터 API 응답이 비어 있습니다.");
        }

        String trimmed = responseBody.trim();

        try {
            if (trimmed.startsWith("<")) {
                return xmlMapper.readTree(trimmed);
            }

            return objectMapper.readTree(trimmed);
        } catch (Exception e) {
            log.error("공공데이터 API 응답 파싱 실패", e);
            throw new RuntimeException("공공데이터 API 응답을 파싱하지 못했습니다.");
        }
    }
}