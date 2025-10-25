package com.schnofiticationbe.dto;

import com.schnofiticationbe.exception.ErrorCode;

public record ErrorResponse(String code, String message) {
    // 예외 발생 시 ErrorCode를 받아 json형태 정의
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.name(), errorCode.getMessage());
    }

    // HttpStatus와 메시지로 직접 생성할 수 있는 오버로드 추가
    public static ErrorResponse of(org.springframework.http.HttpStatus status, String message) {
        String msg = (message == null || message.isBlank()) ? status.getReasonPhrase() : message;
        return new ErrorResponse(status.name(), msg);
    }
}