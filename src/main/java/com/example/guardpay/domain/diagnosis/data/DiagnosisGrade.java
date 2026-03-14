package com.example.guardpay.domain.diagnosis.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DiagnosisGrade {
    MASTER(90, "안전 송금 마스터"),
    SHIELD(70, "금융 방패단"),
    NOVICE(50, "초보 금융가"),
    CAUTION(0, "주의 필요");

    private final int minScore;
    private final String description;


    public static DiagnosisGrade fromScore(int score) {
        return Arrays.stream(DiagnosisGrade.values())
                .filter(grade -> score >= grade.minScore)
                .findFirst()
                .orElse(CAUTION);
    }
}
