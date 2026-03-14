package com.example.guardpay.domain.chatbot.service;

import com.example.guardpay.domain.GeminiConfig;
import com.example.guardpay.domain.chatbot.dto.req.GeminiRequest;
import com.example.guardpay.domain.chatbot.dto.res.GeminiResponse;
import com.example.guardpay.domain.chatbot.enums.ChatErrorCode;
import com.example.guardpay.domain.chatbot.exception.ChatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;



@Slf4j
@Component
@RequiredArgsConstructor
class GeminiClientService {

    private final GeminiConfig geminiConfig;
    private final WebClient webClient;

    public String getCompletion(String systemPrompt, String userPrompt) {
        String url = String.format("https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s",
                geminiConfig.getModel(), geminiConfig.getKey());

        String finalPrompt = combineAndTruncate(systemPrompt, userPrompt);

        GeminiRequest requestBody = GeminiRequest.of(finalPrompt);

        try {
            GeminiResponse response = webClient.post()
                    .uri(url)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .block();

            return parseResponse(response);

        } catch (Exception e) {
            log.error("Gemini API call failed", e);
            throw new ChatException(ChatErrorCode.CHAT_API_ERROR);
        }
    }

    private String parseResponse(GeminiResponse response) {
        if (response == null) throw new ChatException(ChatErrorCode.CHAT_API_ERROR);

        if (isSafetyBlocked(response)) throw new ChatException(ChatErrorCode.SAFETY_BLOCK);

        try {
            return response.candidates().get(0).content().parts().get(0).text();
        } catch (Exception e) {
            throw new ChatException(ChatErrorCode.EMPTY_RESPONSE);
        }
    }

    private boolean isSafetyBlocked(GeminiResponse response) {
        // finishReason 체크
        if (response.candidates() != null && !response.candidates().isEmpty()) {
            if ("SAFETY".equals(response.candidates().get(0).finishReason())) return true;
        }
        // blockReason 체크
        return response.promptFeedback() != null && "SAFETY".equals(response.promptFeedback().blockReason());
    }

    private String combineAndTruncate(String system, String user) {
        String combined = system + "\n\n" + user;
        return combined.length() > 30000 ? combined.substring(0, 30000) : combined;
    }

}