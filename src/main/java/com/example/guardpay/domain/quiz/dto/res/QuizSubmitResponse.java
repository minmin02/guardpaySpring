package com.example.guardpay.domain.quiz.dto.res;

public record QuizSubmitResponse(
        boolean isCorrect,
        int gainExp,
        String message
) {}
