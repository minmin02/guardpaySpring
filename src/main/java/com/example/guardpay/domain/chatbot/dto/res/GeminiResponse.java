package com.example.guardpay.domain.chatbot.dto.res;

import java.util.List;

public record GeminiResponse(
        List<Candidate> candidates,
        PromptFeedback promptFeedback
) {
    public record Candidate(Content content, String finishReason) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
    public record PromptFeedback(String blockReason) {}
}