package com.payment.system.authentication_service.util.exceptions;

import com.payment.system.authentication_service.util.MessageUtil;

public class BusinessException extends Exception {

    public BusinessException(String code) {
        super(MessageUtil.getMessage(code));
    }
}
