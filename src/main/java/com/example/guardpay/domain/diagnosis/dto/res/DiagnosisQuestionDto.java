package com.example.guardpay.domain.diagnosis.dto.res;

import java.util.List;

public record DiagnosisQuestionDto(
        Long questionId,
        String questionText,
        List<DiagnosisOptionDto> options
) {}