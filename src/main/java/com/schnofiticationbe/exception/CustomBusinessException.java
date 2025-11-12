package com.schnofiticationbe.exception;

import lombok.Getter;

@Getter
public class CustomBusinessException extends RuntimeException {
    // Exception 발생 시 ErrorCode를 통해 메시지 관리
    private final ErrorCode errorCode;

    public CustomBusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}