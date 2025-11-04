package com.example.guardpay.domain.chatbot.service;

import com.example.guardpay.domain.GeminiConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialEducationService {

    private final ResourceLoader resourceLoader;
    private final GeminiConfig geminiConfig;
    private final WebClient webClient;

    private String systemPrompt;
    private String userPromptTemplate;

    @PostConstruct
    public void loadPrompts() throws IOException {
        log.info("📚 Loading financial education prompts...");

        Resource systemResource = resourceLoader.getResource(
                "classpath:prompts/financial-education-system.txt"
        );
        this.systemPrompt = StreamUtils.copyToString(
                systemResource.getInputStream(),
                StandardCharsets.UTF_8
        );

        Resource userResource = resourceLoader.getResource(
                "classpath:prompts/financial-education-user.txt"
        );
        this.userPromptTemplate = StreamUtils.copyToString(
                userResource.getInputStream(),
                StandardCharsets.UTF_8
        );

        log.info("✅ Prompts loaded successfully");
        log.info("   - System prompt length: {} chars", systemPrompt.length());
        log.info("   - User template length: {} chars", userPromptTemplate.length());
    }

    public String generateEducationContent(
            String userQuery,
            String analysisData,
            String ageGroup,
            String interests
    ) {
        log.info("📚 Generating financial education content...");
        log.info("   - Query: {}", userQuery);

        String userPrompt = userPromptTemplate
                .replace("{ANALYSIS_DATA}", analysisData != null ? analysisData : "분석 데이터 없음")
                .replace("{USER_QUERY}", userQuery)
                .replace("{TIMESTAMP}", LocalDateTime.now().toString())
                .replace("{AGE_GROUP}", ageGroup != null ? ageGroup : "20-30대")
                .replace("{INTERESTS}", interests != null ? interests : "일반");

        return callGeminiApi(systemPrompt, userPrompt);
    }

    private String callGeminiApi(String systemPrompt, String userPrompt) {
        String url = String.format(
                "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                geminiConfig.getModel(),
                geminiConfig.getKey()
        );

        // ✅ 프롬프트 길이 체크
        String fullPrompt = systemPrompt + "\n\n" + userPrompt;
        log.info("📝 Full prompt length: {} chars", fullPrompt.length());

        // ✅ 프롬프트가 너무 길면 잘라내기 (Gemini 토큰 제한 대비)
        if (fullPrompt.length() > 30000) {
            log.warn("⚠️ Prompt too long, truncating to 30000 chars");
            fullPrompt = fullPrompt.substring(0, 30000);
        }

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", fullPrompt)
                                )
                        )
                ),
                "generationConfig", Map.of(
                        "temperature", 0.7,
                        "topK", 40,
                        "topP", 0.95,
                        "maxOutputTokens", 2048
                )
        );

        try {
            log.info("🔄 Calling Gemini API...");
            log.info("   - URL: {}", url.substring(0, url.indexOf("?key="))); // API 키 제외
            log.info("   - Model: {}", geminiConfig.getModel());

            Map<String, Object> response = webClient.post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // ✅ 전체 응답 로그 출력 (디버깅용)
            log.info("📥 Full Gemini Response: {}", response);

            // ✅ 응답 구조 확인
            if (response == null) {
                log.error("❌ Response is null");
                throw new RuntimeException("Gemini API returned null response");
            }

            // ✅ 에러 응답 체크
            if (response.containsKey("error")) {
                Map<String, Object> error = (Map<String, Object>) response.get("error");
                String errorMessage = (String) error.get("message");
                String errorCode = String.valueOf(error.get("code"));
                log.error("❌ Gemini API Error: {} (code: {})", errorMessage, errorCode);
                throw new RuntimeException("Gemini API Error: " + errorMessage);
            }

            // ✅ candidates 확인
            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                log.error("❌ No candidates in response");
                log.error("   - Response keys: {}", response.keySet());

                // promptFeedback 확인 (안전 필터링으로 차단되었을 수 있음)
                if (response.containsKey("promptFeedback")) {
                    Map<String, Object> feedback = (Map<String, Object>) response.get("promptFeedback");
                    log.error("   - Prompt Feedback: {}", feedback);
                }

                throw new RuntimeException("Gemini API returned empty candidates");
            }

            log.info("✅ Found {} candidate(s)", candidates.size());

            // ✅ content 추출
            Map<String, Object> firstCandidate = candidates.get(0);
            log.info("   - First candidate keys: {}", firstCandidate.keySet());

            Map<String, Object> content =
                    (Map<String, Object>) firstCandidate.get("content");

            if (content == null) {
                log.error("❌ No content in first candidate");
                throw new RuntimeException("Gemini API returned candidate without content");
            }

            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) content.get("parts");

            if (parts == null || parts.isEmpty()) {
                log.error("❌ No parts in content");
                throw new RuntimeException("Gemini API returned content without parts");
            }

            String text = (String) parts.get(0).get("text");

            if (text == null || text.isEmpty()) {
                log.error("❌ Empty text in parts");
                throw new RuntimeException("Gemini API returned empty text");
            }

            log.info("✅ Response text length: {} chars", text.length());
            log.info("📄 Response preview: {}...", text.substring(0, Math.min(200, text.length())));

            return text;

        } catch (Exception e) {
            log.error("❌ Gemini API call failed", e);

            // ✅ 상세 에러 정보 출력
            if (e.getMessage() != null) {
                log.error("   - Error message: {}", e.getMessage());
            }

            throw new RuntimeException("Failed to generate education content: " + e.getMessage(), e);
        }
    }
}