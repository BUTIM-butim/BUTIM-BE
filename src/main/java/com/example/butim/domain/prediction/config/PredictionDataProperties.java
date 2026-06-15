package com.example.butim.domain.prediction.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "external.prediction-data")
public class PredictionDataProperties {

    private String baseUrl;
    private String serviceKey;
    private int pageNo;
    private int numOfRows;
    private String industryApplyPath;
    private String industryApprovalPath;
    private String jobApplyPath;
    private String jobApprovalPath;
    private String businessSizeApplyPath;
    private String businessSizeApprovalPath;
    private String ageApplyPath;
    private String ageApprovalPath;
    private String accidentTypeApprovalPath;
    private String diagnosisApplyPath;
    private String diagnosisApprovalPath;
}
