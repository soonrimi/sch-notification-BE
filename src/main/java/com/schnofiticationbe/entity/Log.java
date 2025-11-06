package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(requiredProperties = {"id", "logLevel", "traceId", "clientIp", "requestUri", "httpMethod",
        "message", "requestParams", "requestBody", "requestVariables", "exceptionDetails",
        "httpStatus", "durationMs", "createdAt"})
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;

    @Column(nullable = false, length = 64)
    private String traceId;

    private String clientIp;
    private String requestUri;
    private String httpMethod;

    @Column(columnDefinition = "LONGTEXT")
    private String message;

    @Lob
    private String requestParams;

    @Lob
    private String requestBody;

    @Lob
    private String requestVariables;

    @Lob
    private String exceptionDetails;

    private Integer httpStatus;
    private Long durationMs;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt= Timestamp.valueOf(LocalDateTime.now());

    @Builder
    public Log(LogLevel logLevel, String traceId, String clientIp, String requestUri, String httpMethod,
               String message, String requestParams, String requestBody, String requestVariables,String exceptionDetails,
               Integer httpStatus, Long durationMs) {
        this.logLevel = logLevel;
        this.traceId = traceId;
        this.clientIp = clientIp;
        this.requestUri = requestUri;
        this.httpMethod = httpMethod;
        this.message = message;
        this.requestParams = requestParams;
        this.requestBody = requestBody;
        this.requestVariables = requestVariables;
        this.exceptionDetails = exceptionDetails;
        this.httpStatus = httpStatus;
        this.durationMs = durationMs;
    }
}