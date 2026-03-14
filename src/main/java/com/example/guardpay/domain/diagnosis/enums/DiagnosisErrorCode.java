package com.example.guardpay.domain.diagnosis.enums;

import com.example.guardpay.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiagnosisErrorCode implements ResponseCode {

    DIAGNOSIS_NOT_FOUND("DIAGNOSIS_001", "진단 정보를 찾을 수 없습니다."),
    HISTORY_NOT_FOUND("DIAGNOSIS_002", "존재하지 않는 진단 결과입니다."),
    INVALID_ANSWERS("DIAGNOSIS_003", "제출된 답변 정보가 유효하지 않습니다."),
    ALREADY_DIAGNOSED("DIAGNOSIS_004", "이미 완료된 진단입니다.");

    private final String statusCode;
    private final String message;

}