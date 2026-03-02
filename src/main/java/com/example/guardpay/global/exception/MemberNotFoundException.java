package com.example.guardpay.global.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("가입되지 않은 이메일입니다.");
    }
    public MemberNotFoundException(String message) {
        super(message);
    }
}