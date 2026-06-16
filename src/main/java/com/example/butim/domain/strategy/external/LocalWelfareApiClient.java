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

// 한국사회보장정보원_지자체복지서비스
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalWelfareApiClient {

    private final WebClient.Builder webClientBuilder;
    private final PublicDataXmlParser xmlParser;

    @Value("${external.local-welfare.base-url}")
    private String baseUrl;

    @Value("${external.local-welfare.service-key}")
    private String serviceKey;

    @Value("${external.local-welfare.page-no}")
    private Integer pageNo;

    @Value("${external.local-welfare.num-of-rows}")
    private Integer numOfRows;

    @Value("${external.local-welfare.list-path}")
    private String listPath;

    public List<CandidateSupportDto> search(String sidoName, String sigunguName) {
        try {
            WebClient webClient = webClientBuilder
                    .baseUrl(baseUrl)
                    .build();

            String xml = webClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder
                                .path(listPath)
                                .queryParam("serviceKey", serviceKey)
                                .queryParam("pageNo", pageNo)
                                .queryParam("numOfRows", numOfRows);

                        if (sidoName != null && !sidoName.isBlank()) {
                            builder.queryParam("ctpvNm", sidoName);
                        }

                        if (sigunguName != null && !sigunguName.isBlank()) {
                            builder.queryParam("sggNm", sigunguName);
                        }

                        return builder.build();
                    })
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("지자체복지서비스 API 응답 일부: {}", abbreviate(xml));

            List<CandidateSupportDto> parsedItems = xmlParser.parseWelfareItems(
                    xml,
                    StrategyItemType.LOCAL_WELFARE,
                    "지자체복지서비스"
            );

            log.info(
                    "지자체복지서비스 파싱 결과: {}건, sidoName={}, sigunguName={}",
                    parsedItems.size(),
                    sidoName,
                    sigunguName
            );

            return parsedItems;

        } catch (Exception e) {
            log.error(
                    "지자체복지서비스 API 조회 실패. baseUrl={}, listPath={}, sidoName={}, sigunguName={}",
                    baseUrl,
                    listPath,
                    sidoName,
                    sigunguName,
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