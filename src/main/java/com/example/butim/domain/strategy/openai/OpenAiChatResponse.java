package com.example.butim.domain.strategy.openai;

import java.util.List;

public record OpenAiChatResponse(
        List<Choice> choices
) {
    public record Choice(
            Message message
    ) {
    }

    public record Message(
            String role,
            String content
    ) {
    }

    public String firstContent() {
        if (choices == null || choices.isEmpty()) {
            return null;
        }

        Message message = choices.get(0).message();

        if (message == null) {
            return null;
        }

        return message.content();
    }
}