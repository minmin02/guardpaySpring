package com.example.guardpay.domain.beneficiaries.exception;

import com.example.guardpay.domain.beneficiaries.enums.BeneficiaryErrorCode;
import com.example.guardpay.global.exception.CustomException;

public class BeneficiaryException extends CustomException {
    public BeneficiaryException(BeneficiaryErrorCode errorCode) {
        super(errorCode);
    }
}
