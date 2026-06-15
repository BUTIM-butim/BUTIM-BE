package com.example.butim.domain.prediction.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "external.precedent")
public class PrecedentDataProperties {

    private String baseUrl;
    private String serviceKey;
    private int pageNo;
    private int numOfRows;
    private String countPath;
    private String contentPath;
}
