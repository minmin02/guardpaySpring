package com.example.guardpay.domain.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext; // 👈 [추가]
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.context.Context; // 👈 [추가]

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GeminiChatService {

    private final WebClient webClient;
    private final String geminiApiKey;
    private final String model;

    public GeminiChatService(
            @Value("${gemini.api.key}") String apiKey,
            @Value("${gemini.api.model:gemini-1.5-flash}") String model) {

        this.geminiApiKey = apiKey;
        this.model = model;

        log.info("🔧 [Gemini] Initializing with model: {}", model);

        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    //  [수정] SecurityContext를 파라미터로 받도록 변경
    public Mono<String> getFinancialAdvice(String prompt, SecurityContext context) {
        log.info("📤 [Gemini] Sending request with prompt: {}", prompt);

        //  v1 API로 변경 (v1beta → v1)
        String endpoint = String.format(
                "/v1beta/models/%s:generateContent?key=%s",
                model,
                geminiApiKey
        );

        log.info(" [Gemini] Using endpoint: {}", endpoint);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        return webClient.post()
                .uri(endpoint)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error(" [Gemini] API Error {}: {}", response.statusCode(), body);
                                    return Mono.error(new RuntimeException("Gemini API Error: " + body));
                                }))
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> {
                    try {
                        String text = jsonNode.at("/candidates/0/content/parts/0/text").asText();
                        log.info(" [Gemini] Response: {}", text.substring(0, Math.min(50, text.length())));
                        return text;
                    } catch (Exception e) {
                        log.error(" [Gemini] Failed to parse response: {}", e.getMessage());
                        log.error(" [Gemini] Raw response: {}", jsonNode.toString());
                        return "응답 파싱 실패: " + jsonNode.toString();
                    }
                })
                .doOnError(error -> log.error(" [Gemini] Error: {}", error.getMessage()))
                // [핵심] Reactor 체인에 SecurityContext를 주입합니다.
                .contextWrite(Context.of(SecurityContext.class, context));
    }
}
