package com.example.guardpay.domain.diagnosis.converter;

import com.example.guardpay.domain.diagnosis.dto.res.DiagnosisOptionDto;
import com.example.guardpay.domain.diagnosis.dto.res.DiagnosisQuestionDto;
import com.example.guardpay.domain.quiz.entity.Quiz;

import java.util.List;

public class DiagnosisConverter {
    public static DiagnosisQuestionDto toDiagnosisQuestionDto(Quiz quiz) {
        List<DiagnosisOptionDto> options = quiz.getOptions().stream()
                .map(opt -> new DiagnosisOptionDto(opt.getOptionId(), opt.getOptionText()))
                .toList();

        return new DiagnosisQuestionDto(
                quiz.getQuizId(),
                quiz.getQuestion(),
                options
        );
    }
}
