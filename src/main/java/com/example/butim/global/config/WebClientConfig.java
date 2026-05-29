package com.example.butim.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(this::configureCodecs)
                .build();

        return WebClient.builder()
                .exchangeStrategies(strategies);
    }

    private void configureCodecs(CodecConfigurer configurer) {
        configurer.defaultCodecs().maxInMemorySize(5 * 1024 * 1024);
    }
}