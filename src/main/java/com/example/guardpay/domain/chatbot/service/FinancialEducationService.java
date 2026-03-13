package com.example.guardpay.domain.chatbot.service;

import com.example.guardpay.domain.GeminiConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${gemini.prompts.financial-education.system}")
    private String systemPromptPath;

    @Value("${gemini.prompts.financial-education.user}")
    private String userPromptPath;

    private String systemPrompt;
    private String userPromptTemplate;

    @PostConstruct
    public void loadPrompts() throws IOException {
        log.info("📚 Loading financial education prompts...");

        Resource systemResource = resourceLoader.getResource(systemPromptPath);

        if (!systemResource.exists()) {
            log.error(" System prompt file not found at: {}", systemPromptPath);
            throw new IOException("System prompt file not found: " + systemPromptPath);
        }

        this.systemPrompt = StreamUtils.copyToString(
                systemResource.getInputStream(),
                StandardCharsets.UTF_8
        );

        Resource userResource = resourceLoader.getResource(userPromptPath);

        if (!userResource.exists()) {
            log.error(" User prompt file not found at: {}", userPromptPath);
            throw new IOException("User prompt file not found: " + userPromptPath);
        }

        this.userPromptTemplate = StreamUtils.copyToString(
                userResource.getInputStream(),
                StandardCharsets.UTF_8
        );

        log.info(" Prompts loaded successfully");
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

        //  프롬프트 길이 체크
        String fullPrompt = systemPrompt + "\n\n" + userPrompt;
        log.info("📝 Full prompt length: {} chars", fullPrompt.length());

        //  프롬프트가 너무 길면 잘라내기 (Gemini 토큰 제한 대비)
        if (fullPrompt.length() > 30000) {
            log.warn("️ Prompt too long, truncating to 30000 chars");
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
                        "maxOutputTokens", 8192  //  답변 길이 증가 (2048 → 8192)
                ),
                //  안전 설정 완화 추가
                "safetySettings", List.of(
                        Map.of(
                                "category", "HARM_CATEGORY_HARASSMENT",
                                "threshold", "BLOCK_NONE"
                        ),
                        Map.of(
                                "category", "HARM_CATEGORY_HATE_SPEECH",
                                "threshold", "BLOCK_NONE"
                        ),
                        Map.of(
                                "category", "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                                "threshold", "BLOCK_NONE"
                        ),
                        Map.of(
                                "category", "HARM_CATEGORY_DANGEROUS_CONTENT",
                                "threshold", "BLOCK_NONE"
                        )
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

            //  전체 응답 로그 출력 (디버깅용)
            log.info(" Full Gemini Response: {}", response);

            //  응답 구조 확인
            if (response == null) {
                log.error(" Response is null");
                return "죄송합니다. 서버 응답이 없습니다. 잠시 후 다시 시도해 주세요.";
            }

            //  에러 응답 체크
            if (response.containsKey("error")) {
                Map<String, Object> error = (Map<String, Object>) response.get("error");
                String errorMessage = (String) error.get("message");
                String errorCode = String.valueOf(error.get("code"));
                log.error(" Gemini API Error: {} (code: {})", errorMessage, errorCode);

                // 사용자 친화적 메시지 반환
                if (errorCode.equals("400")) {
                    return "죄송합니다. 요청 형식이 올바르지 않습니다. 관리자에게 문의하세요.";
                } else if (errorCode.equals("429")) {
                    return "죄송합니다. 요청이 너무 많습니다. 잠시 후 다시 시도해 주세요.";
                } else if (errorCode.equals("500")) {
                    return "죄송합니다. 서버에 일시적인 문제가 있습니다. 잠시 후 다시 시도해 주세요.";
                }
                return "죄송합니다. 오류가 발생했습니다: " + errorMessage;
            }

            // candidates 확인
            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                log.error(" No candidates in response");

                // promptFeedback 확인
                if (response.containsKey("promptFeedback")) {
                    Map<String, Object> feedback = (Map<String, Object>) response.get("promptFeedback");
                    log.error("   - Prompt Feedback: {}", feedback);

                    String blockReason = (String) feedback.get("blockReason");
                    if ("SAFETY".equals(blockReason)) {
                        return "죄송합니다. 안전 정책으로 인해 해당 질문에 답변할 수 없습니다. 다른 방식으로 질문해 주세요.";
                    }
                }

                return "죄송합니다. 응답을 생성할 수 없습니다. 다시 시도해 주세요.";
            }

            log.info(" Found {} candidate(s)", candidates.size());

            // content 추출
            Map<String, Object> firstCandidate = candidates.get(0);
            log.info("   - First candidate keys: {}", firstCandidate.keySet());

            //  finishReason 체크 (안전 필터 확인)
            String finishReason = (String) firstCandidate.get("finishReason");
            log.info("   - Finish reason: {}", finishReason);

            if ("SAFETY".equals(finishReason)) {
                log.error(" Response blocked by safety filter");
                List<Map<String, Object>> safetyRatings =
                        (List<Map<String, Object>>) firstCandidate.get("safetyRatings");
                if (safetyRatings != null) {
                    log.error("   - Safety ratings: {}", safetyRatings);
                }
                return "죄송합니다. 안전 정책으로 인해 해당 질문에 답변할 수 없습니다. 다른 방식으로 질문해 주세요.";
            }

            if ("RECITATION".equals(finishReason)) {
                log.error(" Response blocked due to recitation");
                return "죄송합니다. 저작권 문제로 인해 응답을 생성할 수 없습니다. 다른 질문을 시도해 주세요.";
            }

            Map<String, Object> content =
                    (Map<String, Object>) firstCandidate.get("content");

            if (content == null) {
                log.error(" No content in first candidate");
                log.error("   - Candidate data: {}", firstCandidate);
                return "죄송합니다. 응답 내용이 없습니다. 다시 시도해 주세요.";
            }

            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) content.get("parts");

            if (parts == null || parts.isEmpty()) {
                log.error(" No parts in content");
                log.error("   - Content data: {}", content);
                log.error("   - Finish reason: {}", finishReason);
                return "죄송합니다. 응답 데이터가 비어있습니다. 질문을 다시 작성해 주세요.";
            }

            String text = (String) parts.get(0).get("text");

            if (text == null || text.isEmpty()) {
                log.error(" Empty text in parts");
                return "죄송합니다. 빈 응답을 받았습니다. 다시 시도해 주세요.";
            }

            log.info(" Response text length: {} chars", text.length());
            log.info(" Response preview: {}...", text.substring(0, Math.min(200, text.length())));

            return text;

        } catch (Exception e) {
            log.error(" Gemini API call failed", e);

            //  상세 에러 정보 출력
            if (e.getMessage() != null) {
                log.error("   - Error message: {}", e.getMessage());
            }

            //  사용자 친화적 에러 메시지 반환 (500 오류 방지)
            return "죄송합니다. 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.";
        }
    }
}