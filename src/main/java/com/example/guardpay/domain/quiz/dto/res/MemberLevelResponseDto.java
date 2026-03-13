package com.example.guardpay.domain.quiz.dto.res;

public record MemberLevelResponseDto(
        Long categoryId,
        String categoryName,
        Integer level
) {}