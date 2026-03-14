package com.example.guardpay.domain.chatbot.controller;

import com.example.guardpay.domain.chatbot.dto.req.ChatRequestDto;
import com.example.guardpay.domain.chatbot.dto.res.ChatResponseDto;
import com.example.guardpay.domain.chatbot.service.FinancialEducationService; // ← 변경
import com.example.guardpay.global.jwt.MemberUserDetails;
import com.example.guardpay.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "챗봇", description = "챗봇 서비스")
public class ChatBotController {

    private final FinancialEducationService financialEducationService;

    @Operation(summary = "챗봇 입장 API", description = "프롬프트를 받고 작업시작")
    @PostMapping("/financial-advice")
    public ApiResponse<ChatResponseDto> getAdvice(
            @RequestBody ChatRequestDto requestDto,
            @AuthenticationPrincipal MemberUserDetails userDetails) {


        String responseText = financialEducationService.generateEducationContent(
                requestDto.getPrompt(),
                null,
                "20-30대",
                "일반"
        );

        return ApiResponse.ok(new ChatResponseDto(responseText));
    }


}