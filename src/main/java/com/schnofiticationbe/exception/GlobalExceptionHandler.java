package com.schnofiticationbe.exception;

import com.schnofiticationbe.config.LogContextHolder;
import com.schnofiticationbe.dto.ErrorResponse;
import com.schnofiticationbe.entity.Log;
import com.schnofiticationbe.entity.LogLevel;
import com.schnofiticationbe.service.DiscordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LogContextHolder logContextHolder;
    private final DiscordService discordService;

    /**
     * ResponseStatusException 처리
     * 400번대(클라이언트 실수)는 경고만, 500번대(서버 실수)는 알림 발송
     */
    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e, HttpServletRequest request) {
        log.warn("ResponseStatusException caught: {} {}", e.getStatusCode(), e.getReason());

        boolean isServerError = e.getStatusCode().is5xxServerError();

        String traceId = UUID.randomUUID().toString().substring(0, 8);

        Log.LogBuilder logBuilder = logContextHolder.get();
        if (logBuilder != null) {
            logBuilder.logLevel(isServerError ? LogLevel.ERROR : LogLevel.WARN)
                    .message(e.getReason())
                    .httpStatus(e.getStatusCode().value())
                    .traceId(traceId)
                    .exceptionDetails(getStackTraceAsString(e));
        }

        if (isServerError) {
            String title = "🚨 ResponseStatusException (" + e.getStatusCode().value() + ")";
            sendDiscordAlert(title, e, request, traceId);
        }

        HttpStatus resolvedStatus = HttpStatus.resolve(e.getStatusCode().value());
        if (resolvedStatus == null) {
            resolvedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(ErrorResponse.of(resolvedStatus, e.getReason()), resolvedStatus);
    }

    /**
     * 비즈니스 예외 (CustomBusinessException)
     */
    @ExceptionHandler(CustomBusinessException.class)
    protected ResponseEntity<ErrorResponse> handleCustomBusinessException(CustomBusinessException e, HttpServletRequest request) {
        log.warn("CustomBusinessException caught: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();

        Log.LogBuilder logBuilder = logContextHolder.get();
        if (logBuilder != null) {
            logBuilder.logLevel(LogLevel.WARN)
                    .message(e.getMessage())
                    .exceptionDetails(getStackTraceAsString(e));
        }

        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getStatus());
    }

    /**
     * 404 Not Found (NoResourceFoundException)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        log.warn("NoResourceFoundException caught: {}", e.getMessage());
        ErrorCode errorCode = ErrorCode.BOARD_NOT_FOUND;

        Log.LogBuilder logBuilder = logContextHolder.get();
        if (logBuilder != null) {
            logBuilder.logLevel(LogLevel.WARN)
                    .message(e.getMessage())
                    .httpStatus(errorCode.getStatus().value()); // 상태 코드도 직접 설정
        }

        return new ResponseEntity<>(ErrorResponse.of(errorCode), errorCode.getStatus());
    }

    /**
     * 예상치 못한 모든 예외 (Exception)
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Unhandled Exception caught: {}", e.getMessage(), e);

        String traceId = UUID.randomUUID().toString().substring(0, 8);

        Log.LogBuilder logBuilder = logContextHolder.get();
        if (logBuilder != null) {
            logBuilder.logLevel(LogLevel.ERROR)
                    .message(e.getMessage())
                    .traceId(traceId) // DB 저장용
                    .exceptionDetails(getStackTraceAsString(e));
        }

        sendDiscordAlert("🔥 Unhandled Exception (500)", e, request, traceId);

        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR), ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }

    private void sendDiscordAlert(String title, Exception e, HttpServletRequest request, String traceId) {
        String stackTrace = getStackTraceAsString(e);

        if (stackTrace != null && stackTrace.length() > 2000) {
            stackTrace = stackTrace.substring(0, 2000) + "...(truncated)";
        }

        discordService.sendErrorAlert(
                title,
                (e.getMessage() != null ? e.getMessage() : "No Error Message")
                        + "\n```java\n" + stackTrace + "\n```",
                request.getRequestURI(),
                traceId
        );
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("HttpRequestMethodNotSupportedException caught: {}", e.getMessage());

        Log.LogBuilder logBuilder = logContextHolder.get();
        if (logBuilder != null) {
            logBuilder.logLevel(LogLevel.WARN)
                    .message(e.getMessage())
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        }

        return new ResponseEntity<>(
                ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    private String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) return null;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}