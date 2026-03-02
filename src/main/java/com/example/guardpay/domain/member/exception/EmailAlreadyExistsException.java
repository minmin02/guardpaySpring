package com.example.guardpay.domain.member.exception;


import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.global.exception.CustomException;

public class EmailAlreadyExistsException extends CustomException {

    public EmailAlreadyExistsException() {
        super(MemberErrorCode.EMAIL_ALREADY_EXISTS);
    }

    public EmailAlreadyExistsException(String description) {
        super(MemberErrorCode.EMAIL_ALREADY_EXISTS, description);
    }
}