package com.example.butim.domain.strategy.external;

import com.example.butim.domain.strategy.dto.common.CandidateSupportDto;
import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

// 서민금융진흥원_대출상품한눈에 정보 서비스
@Slf4j
@Component
@RequiredArgsConstructor
public class MicroFinanceApiClient {

    private final WebClient.Builder webClientBuilder;
    private final PublicDataXmlParser xmlParser;

    @Value("${external.micro-finance.base-url}")
    private String baseUrl;

    @Value("${external.micro-finance.service-key}")
    private String serviceKey;

    @Value("${external.micro-finance.page-no}")
    private Integer pageNo;

    @Value("${external.micro-finance.num-of-rows}")
    private Integer numOfRows;

    @Value("${external.micro-finance.list-path}")
    private String listPath;

    public List<CandidateSupportDto> search() {
        try {
            WebClient webClient = webClientBuilder
                    .baseUrl(baseUrl)
                    .build();

            String xml = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(listPath)
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("pageNo", pageNo)
                            .queryParam("numOfRows", numOfRows)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .map(body -> {
                                        log.error("서민금융 API 오류 응답 status={}, body={}",
                                                response.statusCode(), body);
                                        return new RuntimeException(body);
                                    })
                    )
                    .bodyToMono(String.class)
                    .block();

            log.info("서민금융 API 응답 일부: {}", abbreviate(xml));

            return xmlParser.parseLoanItems(xml);

        } catch (Exception e) {
            log.error("서민금융 API 조회 실패. baseUrl={}, listPath={}", baseUrl, listPath, e);
            throw new CustomException(ErrorCode.PUBLIC_DATA_REQUEST_FAILED);
        }
    }

    private String abbreviate(String value) {
        if (value == null) {
            return null;
        }

        return value.length() > 500 ? value.substring(0, 500) : value;
    }
}