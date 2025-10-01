package com.schnofiticationbe.dto;

import com.schnofiticationbe.exception.ErrorCode;

public record ErrorResponse(String code, String message) {
    // 예외 발생 시 ErrorCode를 받아 json형태 정의
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }
}