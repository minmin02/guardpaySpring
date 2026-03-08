package com.example.guardpay.domain.shop.exeption;

import com.example.guardpay.domain.shop.enums.ShopErrorCode;
import com.example.guardpay.global.exception.CustomException;

public class ShopException extends CustomException {
    public ShopException(ShopErrorCode errorCode) {
        super(errorCode);
    }
}
