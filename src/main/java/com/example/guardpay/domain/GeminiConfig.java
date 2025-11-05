package com.example.guardpay.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ConfigurationProperties(prefix = "gemini.api")
@Data
public class GeminiConfig {
    private String key;      // ← YAML의 'key'와 매핑
    private String model;    // ← YAML의 'model'과 매핑

    // ✅ Lombok @Data가 자동 생성:
    // - getKey()
    // - getModel()
    // - setKey(String key)
    // - setModel(String model)

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}