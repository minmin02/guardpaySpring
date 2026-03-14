package com.example.guardpay.domain.diagnosis.dto.res;

public record DiagnosisHistoryResponse(
        Long historyId,
        int score,
        String finalGrade
) {}