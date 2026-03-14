package com.example.guardpay.domain.beneficiaries.enums;

import com.example.guardpay.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BeneficiaryErrorCode implements ResponseCode {
    BENEFICIARY_NOT_FOUND("BENEFICIARY_001", "존재하지 않는 가상계좌입니다."),
    INSUFFICIENT_BALANCE("BENEFICIARY_002", "잔액이 부족하여 송금할 수 없습니다."),
    TRANSFER_FAILED("BENEFICIARY_003", "송금 처리 중 오류가 발생했습니다.");

    private final String statusCode;
    private final String message;
}