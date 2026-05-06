package com.example.butim.domain.industry.dto;

import com.example.butim.domain.industry.entity.Industry;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IndustryResponse {

    private Long industryId;
    private String industryName;
    private String industryCode;

    public static IndustryResponse from(Industry industry) {
        return IndustryResponse.builder()
                .industryId(industry.getId())
                .industryName(industry.getIndustryName())
                .industryCode(industry.getIndustryCode())
                .build();
    }
}