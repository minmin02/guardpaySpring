package com.example.guardpay.domain.diagnosis.exception;

import com.example.guardpay.domain.diagnosis.enums.DiagnosisErrorCode;
import com.example.guardpay.global.exception.CustomException;

public class DiagnosisException extends CustomException {

    public DiagnosisException(DiagnosisErrorCode errorCode) {
        super(errorCode);
    }

}
