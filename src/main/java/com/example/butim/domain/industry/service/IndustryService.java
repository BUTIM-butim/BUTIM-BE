package com.example.butim.domain.industry.service;

import com.example.butim.domain.industry.client.IndustrialAccidentApiClient;
import com.example.butim.domain.industry.dto.IndustryResponse;
import com.example.butim.domain.industry.dto.JobResponse;
import com.example.butim.domain.industry.entity.Industry;
import com.example.butim.domain.industry.entity.Job;
import com.example.butim.domain.industry.repository.IndustryRepository;
import com.example.butim.domain.industry.repository.JobRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndustryService {

    private final IndustryRepository industryRepository;
    private final JobRepository jobRepository;
    private final IndustrialAccidentApiClient apiClient;

    @Transactional(readOnly = true)
    public List<IndustryResponse> getIndustries() {
        return industryRepository.findAllByOrderByIndustryNameAsc()
                .stream()
                .map(IndustryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<JobResponse> getJobs() {
        return jobRepository.findAllByOrderByJobNameAsc()
                .stream()
                .map(JobResponse::from)
                .toList();
    }

    @Transactional
    public void syncIndustriesAndJobs() {
        syncIndustries();
        syncJobs();
    }

    private void syncIndustries() {
        JsonNode root = apiClient.fetchIndustryData();

        log.info("업종 API 파싱 결과 root = {}", root.toPrettyString());

        List<JsonNode> items = extractItems(root);
        log.info("업종 동기화 대상 item 수: {}", items.size());

        int savedCount = 0;
        int skippedCount = 0;

        for (JsonNode item : items) {
            String industryName = extractIndustryName(item);
            String industryCode = extractIndustryCode(item);

            if (industryName == null || industryName.isBlank()) {
                skippedCount++;
                log.warn("업종명 추출 실패 item = {}", item.toPrettyString());
                continue;
            }

            if (industryCode == null || industryCode.isBlank()) {
                industryCode = industryName;
            }

            industryName = normalizeText(industryName);
            industryCode = normalizeText(industryCode) + "_" + industryName;

            String finalIndustryName = industryName;
            String finalIndustryCode = industryCode;

            Industry industry = industryRepository.findByIndustryCode(finalIndustryCode)
                    .orElseGet(() -> Industry.builder()
                            .industryName(finalIndustryName)
                            .industryCode(finalIndustryCode)
                            .build());

            industry.updateIndustryName(finalIndustryName);
            industryRepository.save(industry);
            savedCount++;
        }

        log.info("업종 저장 완료 개수: {}, 스킵 개수: {}", savedCount, skippedCount);
    }

    private void syncJobs() {
        JsonNode root = apiClient.fetchJobData();

        log.info("직종 API 파싱 결과 root = {}", root.toPrettyString());

        List<JsonNode> items = extractItems(root);
        log.info("직종 동기화 대상 item 수: {}", items.size());

        int savedCount = 0;
        int skippedCount = 0;

        for (JsonNode item : items) {
            String jobName = extractJobName(item);
            String jobCode = extractJobCode(item);

            if (jobName == null || jobName.isBlank()) {
                skippedCount++;
                log.warn("직종명 추출 실패 item = {}", item.toPrettyString());
                continue;
            }

            if (jobCode == null || jobCode.isBlank()) {
                jobCode = jobName;
            }

            jobName = normalizeText(jobName);
            jobCode = normalizeText(jobCode) + "_" + jobName;

            String finalJobName = jobName;
            String finalJobCode = jobCode;

            Job job = jobRepository.findByJobCode(finalJobCode)
                    .orElseGet(() -> Job.builder()
                            .jobName(finalJobName)
                            .jobCode(finalJobCode)
                            .build());

            job.updateJobName(finalJobName);
            jobRepository.save(job);
            savedCount++;
        }

        log.info("직종 저장 완료 개수: {}, 스킵 개수: {}", savedCount, skippedCount);
    }

    private String extractIndustryName(JsonNode item) {
        return findText(
                item,
                "bplc_tpbiz_nm",
                "bplcTpbizNm",
                "industry_nm",
                "industryName",
                "업종명",
                "업종"
        );
    }

    private String extractIndustryCode(JsonNode item) {
        return findText(
                item,
                "bplc_tpbiz_cd",
                "bplcTpbizCd",
                "industry_cd",
                "industryCode",
                "업종코드",
                "업종 코드"
        );
    }

    private String extractJobName(JsonNode item) {
        return findText(
                item,
                "ocpt_nm",
                "ocptNm",
                "jobName",
                "jobNm",
                "직종명",
                "직종"
        );
    }

    private String extractJobCode(JsonNode item) {
        return findText(
                item,
                "ocpt_cd",
                "ocptCd",
                "jobCode",
                "jobCd",
                "직종코드",
                "직종 코드"
        );
    }

    private String findText(JsonNode node, String... fieldNames) {
        for (String fieldName : fieldNames) {
            JsonNode value = node.get(fieldName);

            if (value != null && !value.isNull()) {
                String text = value.asText();

                if (text != null && !text.isBlank()) {
                    return text.trim();
                }
            }
        }

        return null;
    }

    private String normalizeText(String text) {
        if (text == null) {
            return null;
        }

        return text.trim()
                .replace("?", "·")
                .replaceAll("\\s+", " ");
    }

    private List<JsonNode> extractItems(JsonNode root) {
        if (root == null || root.isNull()) {
            return Collections.emptyList();
        }

        JsonNode rows = root.at("/BODY/ROW");

        if (rows.isMissingNode()) {
            rows = root.at("/body/row");
        }

        if (rows.isMissingNode()) {
            rows = root.at("/response/body/items/item");
        }

        if (rows.isMissingNode()) {
            rows = root.at("/body/items/item");
        }

        if (rows.isMissingNode()) {
            rows = root.at("/items/item");
        }

        if (rows == null || rows.isMissingNode() || rows.isNull()) {
            log.warn("공공데이터 응답에서 ROW/item을 찾지 못했습니다. root = {}", root.toPrettyString());
            return Collections.emptyList();
        }

        if (rows.isArray()) {
            List<JsonNode> result = new ArrayList<>();
            rows.forEach(result::add);
            return result;
        }

        if (rows.isObject()) {
            return List.of(rows);
        }

        return Collections.emptyList();
    }

    private JsonNode findNodeByName(JsonNode node, String targetName) {
        if (node == null || node.isNull()) {
            return null;
        }

        JsonNode direct = node.get(targetName);
        if (direct != null && !direct.isNull()) {
            return direct;
        }

        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode found = findNodeByName(field.getValue(), targetName);

                if (found != null && !found.isNull()) {
                    return found;
                }
            }
        }

        if (node.isArray()) {
            for (JsonNode child : node) {
                JsonNode found = findNodeByName(child, targetName);

                if (found != null && !found.isNull()) {
                    return found;
                }
            }
        }

        return null;
    }
}