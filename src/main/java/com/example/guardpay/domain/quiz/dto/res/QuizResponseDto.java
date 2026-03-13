package com.example.guardpay.domain.quiz.dto.res;

public record QuizResponseDto(
        Long quizId,
        String question,
        Integer level,
        Integer point)
{ }
