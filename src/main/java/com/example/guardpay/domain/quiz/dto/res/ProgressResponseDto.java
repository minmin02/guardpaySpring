package com.example.guardpay.domain.quiz.dto.res;

public record ProgressResponseDto(
        Long categoryId,
        String categoryName,
        Integer progress
) {}