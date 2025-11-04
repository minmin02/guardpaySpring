package com.example.guardpay.domain.quiz.controller;

import com.example.guardpay.domain.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizApiController {

    private final QuizService quizService;

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        return ResponseEntity.ok(quizService.getCategories());
    }

    @GetMapping("/{categoryId}/list")
    public ResponseEntity<Map<String, Object>> getQuizzes(@PathVariable Long categoryId) {
        return ResponseEntity.ok(quizService.getQuizzesByCategory(categoryId));
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<Map<String, Object>> getQuizDetail(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.getQuizDetail(quizId));
    }

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @PathVariable Long quizId,
            @RequestBody Map<String, Long> body
    ) {
        return ResponseEntity.ok(quizService.submitAnswer(quizId, body.get("selectedOptionId")));
    }

    @GetMapping("/history/{memberId}")
    public ResponseEntity<Map<String, Object>> getQuizHistory(@PathVariable Long memberId) {
        return ResponseEntity.ok(quizService.getQuizHistory(memberId));
    }

    @GetMapping("/progress")
    public ResponseEntity<Map<String, Object>> getProgress(@RequestParam Long memberId) {
        return ResponseEntity.ok(quizService.getProgress(memberId));
    }

    @GetMapping("/level")
    public ResponseEntity<Map<String, Object>> getLevels(@RequestParam Long memberId) {
        return ResponseEntity.ok(quizService.getLevels(memberId));
    }
}
