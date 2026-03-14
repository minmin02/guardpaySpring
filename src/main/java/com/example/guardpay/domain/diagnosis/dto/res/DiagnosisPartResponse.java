package com.example.guardpay.domain.diagnosis.dto.res;

import java.util.List;

public record DiagnosisPartResponse(
        Long partId,
        String partName,
        List<DiagnosisQuestionDto> questions
) {}
