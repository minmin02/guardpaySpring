package com.example.guardpay.domain.quiz.controller;

import com.example.guardpay.domain.quiz.dto.req.QuizSubmitRequest;
import com.example.guardpay.domain.quiz.dto.res.*;
import com.example.guardpay.domain.quiz.service.QuizHistoryService;
import com.example.guardpay.domain.quiz.service.QuizQueryService;
import com.example.guardpay.domain.quiz.service.QuizService;
import com.example.guardpay.global.jwt.MemberUserDetails;
import com.example.guardpay.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
@Tag(name = "퀴즈", description = "퀴즈 조회 및 정답 제출")
public class QuizApiController {

    private final QuizService quizService;
    private final QuizHistoryService quizHistoryService;
    private final QuizQueryService quizQueryService;

    @GetMapping("/categories")
    @Operation(summary = "카테고리", description = "카테고리 조회")
    public ApiResponse <List<CategoryResponseDto>> getCategories(@AuthenticationPrincipal MemberUserDetails userDetail) {
        List<CategoryResponseDto> categories = quizQueryService.getCategories();
        return ApiResponse.ok(categories);

    }

    @GetMapping("/{categoryId}/list")
    @Operation(summary = "카테고리 문제 조회", description = "카테고리 아이디별 전체 문제 조회")
    public ApiResponse<QuizListResponse> getQuizzes(@PathVariable Long categoryId,@AuthenticationPrincipal MemberUserDetails userDetail) {
        QuizListResponse response = quizQueryService.getQuizzesByCategory(categoryId);
        return ApiResponse.ok(response);

    }


    @GetMapping("/{quizId}")
    @Operation(summary = "퀴즈", description = "퀴즈 조회")
    public ApiResponse<QuizDetailResponse> getQuizDetail(@PathVariable Long quizId,@AuthenticationPrincipal MemberUserDetails userDetail) {
        QuizDetailResponse response=quizQueryService.getQuizDetail(quizId);
        return ApiResponse.ok(response);
    }


    @PostMapping("/{quizId}/submit")
    @Operation(summary = "퀴즈 제출", description = "퀴즈 정답 제출")
    public ApiResponse<QuizSubmitResponse> submitAnswer(
            @PathVariable Long quizId,
            @RequestBody QuizSubmitRequest request,
            @AuthenticationPrincipal MemberUserDetails userDetail
    ) {
        Long memberId = userDetail.getMember().getMemberId();
        QuizSubmitResponse response = quizService.submitAnswer(memberId, quizId, request);
        return ApiResponse.ok(response);
    }

    @GetMapping("/history")
    @Operation(summary = "퀴즈 이력", description = "퀴즈 제출 이력 조회")
    public ApiResponse<List<QuizHistoryDto>> getQuizHistory(
            @AuthenticationPrincipal MemberUserDetails userDetail
    ) {
        Long memberId = userDetail.getMember().getMemberId();
        List<QuizHistoryDto> response=quizHistoryService.getQuizHistory(memberId);
        return ApiResponse.ok(response);
    }

    @GetMapping("/progress")
    @Operation(summary = "퀴즈 진행률", description = "퀴즈 진행률 조회")
    public ApiResponse<List<ProgressResponseDto>> getProgress(
            @AuthenticationPrincipal MemberUserDetails userDetail
    ) {
        Long memberId = userDetail.getMember().getMemberId();
        List<ProgressResponseDto> response = quizHistoryService.getProgress(memberId);
        return ApiResponse.ok(response);
    }

    @GetMapping("/level")
    @Operation(summary = "퀴즈 레벨", description = "퀴즈 레벨 조회")
    public ApiResponse<List<MemberLevelResponseDto>> getLevels(
            @AuthenticationPrincipal MemberUserDetails userDetail
    ) {
        Long memberId = userDetail.getMember().getMemberId();
        List<MemberLevelResponseDto> response = quizHistoryService.getLevels(memberId);
        return ApiResponse.ok(response);
    }

}
