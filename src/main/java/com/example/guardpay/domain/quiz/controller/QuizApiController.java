package com.example.guardpay.domain.quiz.controller;

import com.example.guardpay.domain.quiz.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
@Tag(name = "퀴즈", description = "퀴즈 조회 및 정답 제출")
public class QuizApiController {

    private final QuizService quizService;

    @GetMapping("/categories")
    @Operation(summary = "카테고리", description = "카테고리 조회")
    public ResponseEntity<Map<String, Object>> getCategories() {
        return ResponseEntity.ok(quizService.getCategories());
    }

    @GetMapping("/{categoryId}/list")
    @Operation(summary = "카테고리 문제 조회", description = "카테고리 아이디별 전체 문제 조회")
    public ResponseEntity<Map<String, Object>> getQuizzes(@PathVariable Long categoryId) {
        return ResponseEntity.ok(quizService.getQuizzesByCategory(categoryId));
    }

    @GetMapping("/{quizId}")
    @Operation(summary = "퀴즈", description = "퀴즈 조회")
    public ResponseEntity<Map<String, Object>> getQuizDetail(@PathVariable Long quizId) {
        try {
            return ResponseEntity.ok(quizService.getQuizDetail(quizId));
        } catch (IllegalArgumentException e) {
            // 없는 퀴즈일 때
            Map<String, Object> error = new HashMap<>();
            error.put("status", 404);
            error.put("message", e.getMessage());
            return ResponseEntity.status(404).body(error);
        }
    }

    // ✅ JWT 토큰 기반 퀴즈 정답 제출
    @PostMapping("/{quizId}/submit")
    @Operation(summary = "퀴즈 제출", description = "퀴즈 정답 제출")
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @PathVariable Long quizId,
            @RequestBody Map<String, Long> body,
            @AuthenticationPrincipal User user // JWT에서 인증된 유저 정보
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        return ResponseEntity.ok(quizService.submitAnswer(memberId, quizId, body.get("selectedOptionId")));
    }

    @GetMapping("/history")
    @Operation(summary = "퀴즈 이력", description = "퀴즈 제출 이력 조회")
    public ResponseEntity<Map<String, Object>> getQuizHistory(
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        return ResponseEntity.ok(quizService.getQuizHistory(memberId));
    }

    @GetMapping("/progress")
    @Operation(summary = "퀴즈 진행률", description = "퀴즈 진행률 조회")
    public ResponseEntity<Map<String, Object>> getProgress(@AuthenticationPrincipal User user) {
        Long memberId = Long.parseLong(user.getUsername());
        return ResponseEntity.ok(quizService.getProgress(memberId));
    }

    @GetMapping("/level")
    @Operation(summary = "퀴즈 레벨", description = "퀴즈 레벨 조회")
    public ResponseEntity<Map<String, Object>> getLevels(@AuthenticationPrincipal User user) {
        Long memberId = Long.parseLong(user.getUsername());
        return ResponseEntity.ok(quizService.getLevels(memberId));
    }
}
