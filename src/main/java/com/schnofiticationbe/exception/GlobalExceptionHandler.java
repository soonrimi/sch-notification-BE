package com.schnofiticationbe.exception;

import com.schnofiticationbe.config.LogContextHolder;
import com.schnofiticationbe.dto.ErrorResponse;
import com.schnofiticationbe.entity.Log;
import com.schnofiticationbe.entity.LogLevel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @file GlobalExceptionHandler.java
 * @brief 애플리케이션 전역에서 발생하는 모든 예외를 붙잡아 처리하는 중앙 처리소 (응급실 역할).
 * @details 예외의 종류에 따라 적절한 HTTP 상태 코드와 에러 메시지를 생성하여 클라이언트에게 반환합니다.
 * 또한, '로그 기록지' (LogContextHolder)에 현재 예외 상태(WARN, ERROR)를 기록하여
 * LoggingInterceptor가 최종 로그를 남길 수 있도록 돕습니다.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogContextHolder logContextHolder;

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e, HttpServletRequest request) {
        log.warn("ResponseStatusException caught: {} {}", e.getStatusCode(), e.getReason());

        Log.LogBuilder logBuilder = logContextHolder.get();
        if (logBuilder != null) {
            logBuilder.logLevel(LogLevel.WARN)
                    .message(e.getReason())
                    .httpStatus(e.getStatusCode().value())
                    .exceptionDetails(getStackTraceAsString(e));
        }

        // HttpStatusCode -> HttpStatus 변환 (표준 상태가 아닐 수 있으므로 안전하게 처리)
        HttpStatus resolvedStatus = HttpStatus.resolve(e.getStatusCode().value());
        if (resolvedStatus == null) {
            resolvedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(ErrorResponse.of(resolvedStatus, e.getReason()), resolvedStatus);
    }

    /**
     * 예측된 비즈니스 예외 (예: 로그인 실패, 잘못된 입력값)를 처리합니다. (내과 의사 역할)
     * 이 예외들은 서버의 잘못이 아니므로 'WARN' 레벨로 처리합니다.
     */
    @ExceptionHandler(CustomBusinessException.class)
    protected ResponseEntity<ErrorResponse> handleCustomBusinessException(CustomBusinessException e, HttpServletRequest request) {
        log.warn("CustomBusinessException caught: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();

        // '로그 기록지'의 상태를 'WARN'으로 업데이트합니다.
        Log.LogBuilder logBuilder = logContextHolder.get();
        if (logBuilder != null) {
            logBuilder.logLevel(LogLevel.WARN)
                    .message(e.getMessage())
                    .exceptionDetails(getStackTraceAsString(e));
        }

        // ErrorCode에 정의된 정확한 상태 코드(400, 401, 404 등)로 응답합니다.
        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getStatus());
    }

    /**
     * 잘못된 URL 요청(404 Not Found)을 처리합니다.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        log.warn("NoResourceFoundException caught: {}", e.getMessage());
        ErrorCode errorCode = ErrorCode.BOARD_NOT_FOUND; // ErrorCode에 NOT_FOUND(404)를 정의해야 합니다.

        Log.LogBuilder logBuilder = logContextHolder.get();
        if (logBuilder != null) {
            logBuilder.logLevel(LogLevel.WARN)
                    .message(e.getMessage())
                    .httpStatus(errorCode.getStatus().value()); // 상태 코드도 직접 설정
        }

        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getStatus());
    }

    /**
     * 위에서 처리되지 않은 모든 예측하지 못한 예외 (예: NullPointerException, DB 오류 등)를 처리합니다. (외과 의사 역할)
     * 이 예외들은 서버의 심각한 문제이므로 'ERROR' 레벨로 처리합니다.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Unhandled Exception caught: {}", e.getMessage(), e);

        // '로그 기록지'의 상태를 'ERROR'로 업데이트합니다.
        Log.LogBuilder logBuilder = logContextHolder.get();
        if (logBuilder != null) {
            logBuilder.logLevel(LogLevel.ERROR)
                    .message(e.getMessage())
                    .exceptionDetails(getStackTraceAsString(e));
        }

        // 예측하지 못한 모든 에러는 500 (내부 서버 오류)으로 응답합니다.
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }

    /**
     * 예외 객체의 전체 스택 트레이스를 문자열로 변환합니다.
     */
    private String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) return null;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}