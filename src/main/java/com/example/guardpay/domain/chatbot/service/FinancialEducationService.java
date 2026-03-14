package com.example.guardpay.domain.chatbot.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialEducationService {

    private final ResourceLoader resourceLoader;
    private final GeminiClientService geminiClientService;

    @Value("${gemini.prompts.financial-education.system}")
    private String systemPromptPath;

    @Value("${gemini.prompts.financial-education.user}")
    private String userPromptPath;

    private String systemPrompt;
    private String userPromptTemplate;

    @PostConstruct
    public void loadPrompts() throws IOException {

        this.systemPrompt = loadResource(systemPromptPath);
        this.userPromptTemplate = loadResource(userPromptPath);

        log.info("Financial Education Prompts Loaded Successfully.");
    }

    public String generateEducationContent(String userQuery, String analysisData, String ageGroup, String interests) {
        String userPrompt = buildPrompt(userQuery, analysisData, ageGroup, interests);
        return geminiClientService.getCompletion(systemPrompt, userPrompt);
    }

    private String buildPrompt(String userQuery, String analysisData, String ageGroup, String interests) {
        return userPromptTemplate
                .replace("{ANALYSIS_DATA}", Objects.requireNonNullElse(analysisData, "분석 데이터 없음"))
                .replace("{USER_QUERY}", userQuery)
                .replace("{TIMESTAMP}", LocalDateTime.now().toString())
                .replace("{AGE_GROUP}", Objects.requireNonNullElse(ageGroup, "20-30대"))
                .replace("{INTERESTS}", Objects.requireNonNullElse(interests, "일반"));
    }

    private String loadResource(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        if (!resource.exists()) throw new IOException("Prompt file not found: " + path);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}