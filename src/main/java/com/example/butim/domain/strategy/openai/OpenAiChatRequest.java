package com.example.butim.domain.strategy.openai;

import java.util.List;
import java.util.Map;

public record OpenAiChatRequest(
        String model,
        List<Message> messages,
        Double temperature,
        Map<String, Object> response_format
) {
    public record Message(
            String role,
            String content
    ) {
    }
}