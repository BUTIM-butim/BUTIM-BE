package com.example.butim.domain.industry.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "external.industrial-accident")
public class IndustrialAccidentProperties {

    private String baseUrl;
    private String serviceKey;
    private int pageNo;
    private int numOfRows;
    private String industryPath;
    private String jobPath;
}