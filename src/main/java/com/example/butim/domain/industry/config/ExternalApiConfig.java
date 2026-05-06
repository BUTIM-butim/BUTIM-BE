package com.example.butim.domain.industry.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IndustrialAccidentProperties.class)
public class ExternalApiConfig {
}