package com.example.butim.domain.strategy.openai;

import com.example.butim.global.exception.CustomException;
import com.example.butim.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.base-url}")
    private String baseUrl;

    @Value("${openai.chat-completions-path}")
    private String chatCompletionsPath;

    public String createStrategyJson(String systemPrompt, String userPrompt) {
        try {
            WebClient webClient = webClientBuilder
                    .baseUrl(baseUrl)
                    .build();

            OpenAiChatRequest request = new OpenAiChatRequest(
                    model,
                    List.of(
                            new OpenAiChatRequest.Message("system", systemPrompt),
                            new OpenAiChatRequest.Message("user", userPrompt)
                    ),
                    0.2,
                    Map.of("type", "json_object")
            );

            OpenAiChatResponse response = webClient.post()
                    .uri(chatCompletionsPath)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OpenAiChatResponse.class)
                    .block();

            if (response == null || response.firstContent() == null) {
                throw new CustomException(ErrorCode.AI_REQUEST_FAILED);
            }

            return response.firstContent();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.AI_REQUEST_FAILED);
        }
    }
}