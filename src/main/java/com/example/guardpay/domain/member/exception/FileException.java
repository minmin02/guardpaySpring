package com.example.guardpay.domain.member.exception;

import com.example.guardpay.domain.member.enums.FileErrorCode;
import com.example.guardpay.domain.member.enums.MemberErrorCode;
import com.example.guardpay.global.exception.CustomException;

public class FileException extends CustomException {
    public FileException(FileErrorCode errorCode) {
        super(errorCode);
    }}
