package com.example.butim.domain.strategy.external;

import com.example.butim.domain.strategy.dto.common.CandidateSupportDto;
import com.example.butim.domain.strategy.enums.StrategyItemType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PublicDataXmlParser {

    private final XmlMapper xmlMapper = new XmlMapper();

    public List<CandidateSupportDto> parseWelfareItems(
            String xml,
            StrategyItemType itemType,
            String source
    ) {
        List<CandidateSupportDto> result = new ArrayList<>();

        try {
            JsonNode root = xmlMapper.readTree(xml.getBytes());

            JsonNode resultCode = root.findValue("resultCode");
            JsonNode resultMsg = root.findValue("resultMsg");

            log.info("{} resultCode={}, resultMsg={}",
                    source,
                    resultCode == null ? null : resultCode.asText(),
                    resultMsg == null ? null : resultMsg.asText()
            );

            JsonNode items = findItemsNode(root);

            if (items == null || items.isNull()) {
                log.warn("{} 응답에서 복지 목록 노드를 찾지 못했습니다. 응답 일부={}",
                        source,
                        abbreviate(xml)
                );
                return result;
            }

            if (items.isArray()) {
                for (JsonNode item : items) {
                    result.add(toWelfareDto(item, itemType, source));
                }
            } else {
                result.add(toWelfareDto(items, itemType, source));
            }

            return result;

        } catch (Exception e) {
            log.error("{} XML 파싱 실패. 응답 일부={}", source, abbreviate(xml), e);
            return result;
        }
    }

    public List<CandidateSupportDto> parseLoanItems(String xml) {
        List<CandidateSupportDto> result = new ArrayList<>();

        try {
            JsonNode root = xmlMapper.readTree(xml.getBytes());
            JsonNode items = root.findValue("item");

            if (items == null || items.isNull()) {
                return result;
            }

            if (items.isArray()) {
                for (JsonNode item : items) {
                    result.add(toLoanDto(item));
                }
            } else {
                result.add(toLoanDto(items));
            }

            return result;

        } catch (Exception e) {
            log.error("서민금융 XML 파싱 실패. 응답 일부={}", abbreviate(xml), e);
            return result;
        }
    }

    private JsonNode findItemsNode(JsonNode root) {
        String[] possibleNodeNames = {
                "item",
                "items",
                "wantedList",
                "servList",
                "serviceList",
                "welfareList",
                "list"
        };

        for (String nodeName : possibleNodeNames) {
            JsonNode node = root.findValue(nodeName);

            if (node != null && !node.isNull()) {
                return node;
            }
        }

        return null;
    }

    private CandidateSupportDto toWelfareDto(
            JsonNode item,
            StrategyItemType type,
            String source
    ) {
        String id = readAny(
                item,
                "servId",
                "serviceId",
                "bizId",
                "id",
                "wlfareInfoId"
        );

        String name = readAny(
                item,
                "servNm",
                "serviceName",
                "wlfareInfoNm",
                "bizNm",
                "name",
                "servName"
        );

        String description = readAny(
                item,
                "servDgst",
                "summary",
                "sprtCn",
                "description",
                "servDtlLink",
                "servDesc",
                "jurMnofNm"
        );

        String url = readAny(
                item,
                "servDtlLink",
                "url",
                "applyUrl",
                "link"
        );

        return new CandidateSupportDto(
                id,
                type,
                name == null ? "복지서비스" : name,
                description == null ? "복지서비스 상세 정보를 확인해 신청 가능 여부를 검토해야 합니다." : description,
                estimateWelfareAmount(name),
                false,
                false,
                url,
                type == StrategyItemType.CENTRAL_WELFARE ? 60 : 45,
                source
        );
    }

    private CandidateSupportDto toLoanDto(JsonNode item) {
        String id = readAny(
                item,
                "seq",
                "finPrdCd",
                "finprdnm",
                "productId",
                "id",
                "loanPrdCd"
        );

        String name = readAny(
                item,
                "finprdnm",
                "finPrdNm",
                "productName",
                "loanPrdNm",
                "name",
                "상품명"
        );

        String description = readAny(
                item,
                "joinWay",
                "description",
                "loanLmt",
                "lnlmt",
                "상품설명",
                "etc"
        );

        String url = readAny(
                item,
                "url",
                "applyUrl",
                "homepage"
        );

        Integer expectedAmount = parseAmount(readAny(item, "lnlmt", "loanLmt", "maxLoanLimit"));

        return new CandidateSupportDto(
                id,
                StrategyItemType.MICRO_FINANCE_LOAN,
                name == null ? "서민금융 대출상품" : name,
                description == null ? "서민금융진흥원 대출상품입니다." : description,
                expectedAmount == null ? 500_000 : expectedAmount,
                true,
                false,
                url,
                30,
                "서민금융진흥원"
        );
    }

    private String readAny(JsonNode node, String... keys) {
        for (String key : keys) {
            JsonNode value = node.get(key);

            if (value != null && !value.isNull()) {
                String text = value.asText();

                if (text != null && !text.isBlank()) {
                    return text;
                }
            }
        }

        return null;
    }

    private Integer estimateWelfareAmount(String name) {
        if (name == null) {
            return 300_000;
        }

        if (name.contains("의료")) {
            return 400_000;
        }
        if (name.contains("생계")) {
            return 600_000;
        }
        if (name.contains("긴급")) {
            return 500_000;
        }
        if (name.contains("전기") || name.contains("연료")) {
            return 300_000;
        }

        return 300_000;
    }

    private Integer parseAmount(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            String onlyNumber = value.replaceAll("[^0-9]", "");

            if (onlyNumber.isBlank()) {
                return null;
            }

            int amount = Integer.parseInt(onlyNumber);

            // 서민금융 API lnlmt가 보통 "만원" 단위로 내려오는 경우가 많아서 원 단위로 변환
            if (amount < 100_000) {
                return amount * 10_000;
            }

            return amount;

        } catch (Exception e) {
            return null;
        }
    }

    private String abbreviate(String value) {
        if (value == null) {
            return null;
        }

        return value.length() > 1000 ? value.substring(0, 1000) : value;
    }
}