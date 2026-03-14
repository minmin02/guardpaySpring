package com.example.guardpay.domain.chatbot.dto.req;

import java.util.List;

public record GeminiRequest(
        List<Content> contents,
        GenerationConfig generationConfig,
        List<SafetySetting> safetySettings
) {
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
    public record GenerationConfig(double temperature, int maxOutputTokens) {}
    public record SafetySetting(String category, String threshold) {}

    // 팩토리 메서드로 생성 로직 캡슐화
    public static GeminiRequest of(String prompt) {
        return new GeminiRequest(
                List.of(new Content(List.of(new Part(prompt)))),
                new GenerationConfig(0.7, 8192),
                List.of("HARM_CATEGORY_HARASSMENT", "HARM_CATEGORY_HATE_SPEECH",
                                "HARM_CATEGORY_SEXUALLY_EXPLICIT", "HARM_CATEGORY_DANGEROUS_CONTENT")
                        .stream().map(c -> new SafetySetting(c, "BLOCK_NONE")).toList()
        );
    }
}