package com.example.guardpay.domain.member.exception;

import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.global.exception.CustomException;

public class MemberException extends CustomException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
