package com.example.butim.domain.strategy.external;

import com.example.butim.domain.strategy.dto.common.CandidateSupportDto;
import com.example.butim.domain.strategy.enums.StrategyItemType;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

// 한국사회보장정보원_중앙부처복지서비스
@Slf4j
@Component
@RequiredArgsConstructor
public class CentralWelfareApiClient {

    private final WebClient.Builder webClientBuilder;
    private final PublicDataXmlParser xmlParser;

    @Value("${external.central-welfare.base-url}")
    private String baseUrl;

    @Value("${external.central-welfare.service-key}")
    private String serviceKey;

    @Value("${external.central-welfare.page-no}")
    private Integer pageNo;

    @Value("${external.central-welfare.num-of-rows}")
    private Integer numOfRows;

    @Value("${external.central-welfare.list-path}")
    private String listPath;

    public List<CandidateSupportDto> search(String keyword) {
        try {
            WebClient webClient = webClientBuilder
                    .baseUrl(baseUrl)
                    .build();

            String xml = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(listPath)
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("callTp", "L")
                            .queryParam("pageNo", pageNo)
                            .queryParam("numOfRows", numOfRows)
                            .queryParam("srchKeyCode", "003")
                            .queryParam("searchWrd", keyword)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("중앙부처복지서비스 API 응답 일부: {}", abbreviate(xml));

            List<CandidateSupportDto> parsedItems = xmlParser.parseWelfareItems(
                    xml,
                    StrategyItemType.CENTRAL_WELFARE,
                    "중앙부처복지서비스"
            );

            log.info(
                    "중앙부처복지서비스 파싱 결과: keyword={}, 전체 {}건",
                    keyword,
                    parsedItems.size()
            );

            return parsedItems;

        } catch (Exception e) {
            log.error(
                    "중앙부처복지서비스 API 조회 실패. baseUrl={}, listPath={}, keyword={}",
                    baseUrl,
                    listPath,
                    keyword,
                    e
            );
            throw new CustomException(ErrorCode.PUBLIC_DATA_REQUEST_FAILED);
        }
    }

    private String abbreviate(String value) {
        if (value == null) {
            return null;
        }

        return value.length() > 1000 ? value.substring(0, 1000) : value;
    }
}