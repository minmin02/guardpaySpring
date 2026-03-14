package com.example.guardpay.domain.chatbot.exception;

import com.example.guardpay.domain.chatbot.enums.ChatErrorCode;
import com.example.guardpay.global.exception.CustomException;

public class ChatException extends CustomException {
    public ChatException(ChatErrorCode errorCode) {
        super(errorCode);
    }
}
