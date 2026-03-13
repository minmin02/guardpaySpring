package com.example.guardpay.domain.quiz.dto.res;

import java.util.List;

public record QuizDetailResponse(Long quizId,
                                 String question,
                                 Integer point,
                                 List<QuizOptionDto> options) {
}
