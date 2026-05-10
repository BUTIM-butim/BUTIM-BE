package com.example.butim.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class OpenAiConfig {

    private final OpenAiProperties openAiProperties;

    @Bean
    public RestClient openAiRestClient() {
        return RestClient.builder()
                .baseUrl(openAiProperties.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + openAiProperties.getApiKey())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
