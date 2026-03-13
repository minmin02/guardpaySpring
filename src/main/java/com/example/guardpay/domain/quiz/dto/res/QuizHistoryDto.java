package com.example.guardpay.domain.quiz.dto.res;

import java.time.LocalDateTime;

public record QuizHistoryDto(Long quizId,
                             String question,
                             Integer gainExp,
                             LocalDateTime answerAt) {
}
