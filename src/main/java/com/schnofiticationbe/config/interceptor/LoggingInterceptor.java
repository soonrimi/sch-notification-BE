package com.schnofiticationbe.config.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schnofiticationbe.config.CustomHttpRequestWrapper;
import com.schnofiticationbe.config.CustomHttpResponseWrapper;
import com.schnofiticationbe.config.LogContextHolder;
import com.schnofiticationbe.entity.Log;
import com.schnofiticationbe.entity.LogLevel;
import com.schnofiticationbe.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

///모든 API 요청/응답을 가로채 로그를 기록하는 인터셉터
@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    // 파일 저장 전용 로거
    private static final Logger fileLogger = LoggerFactory.getLogger("com.schnofiticationbe.file");
    // 콘솔 출력 전용 로거
    private static final Logger consoleLogger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private static final List<String> SENSITIVE_KEYWORDS = List.of("password", "tempPassword", "newPassword", "registerPassword");

    private final ObjectMapper objectMapper;
    private final LogService logService;
    private final LogContextHolder logContextHolder;

    /// 컨트롤러 실행 전에 호출되어 '로그 기록지'를 초기화, 파일에 요청 요약 로깅
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = java.util.UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("startTime", String.valueOf(System.currentTimeMillis()));

        // 정보 수집
        String requestBody = getMaskedRequestBody(request);
        Map<String, String> requestParams = getMaskedRequestParams(request);

        String requestParamsJson = paramsToJson(requestParams);

        Log.LogBuilder logBuilder = Log.builder()
                .traceId(traceId)
                .requestUri(request.getRequestURI())
                .httpMethod(request.getMethod())
                .clientIp(request.getRemoteAddr())
                .requestBody(requestBody)
                .requestParams(requestParamsJson);
        logContextHolder.init(logBuilder);

        // 입력 로그 출력
        String requestLog = createRequestLog(request, requestParams, requestBody);
        consoleLogger.info(requestLog);
        fileLogger.info(requestLog);

        return true;
    }

    ///요청 처리가 완전히 완료된 후에 호출 콘솔, 파일, DB에 각각 다른 전략으로 최종 로그를 기록하고 컨텍스트를 정리
    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) throws Exception {
        try {
            long startTime = Long.parseLong(MDC.get("startTime"));
            long duration = System.currentTimeMillis() - startTime;

            Log.LogBuilder logBuilder = logContextHolder.get();
            if (logBuilder == null) return;

            // 최종 로그 레벨 확정
            LogLevel finalLogLevel = determineLogLevel(logBuilder, ex);

            String responseBody = getResponseBody(res);
            String responseLog = createResponseLog(res.getStatus(), duration, responseBody, ex);

            logToConsole(finalLogLevel, responseLog);

            logToFile(finalLogLevel, res.getStatus(), duration, responseLog);

            // DB 저장
            logBuilder.httpStatus(res.getStatus()).durationMs(duration);
            logService.saveLog(logBuilder.build());

        } finally {
            logContextHolder.clear();
            MDC.clear();
        }
    }


    private String createRequestLog(HttpServletRequest request, Map<String, String> params, String body) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(String.format("ID:[%s] Method:[%s] URL:[%s] Client:[%s] --- Request Start ---",
                MDC.get("traceId"), request.getMethod(), request.getRequestURI(), request.getRemoteAddr()));

        if (params != null && !params.isEmpty()) logMessage.append("\nParams : ").append(params);
        if (body != null && !body.isEmpty()) logMessage.append("\nRequest Body : ").append(body);

        return logMessage.toString();
    }

    private String createResponseLog(int status, long duration, String resBody, Exception ex) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(String.format("ID:[%s] Status:[%d] Duration:[%dms] --- Request End ---",
                MDC.get("traceId"), status, duration));

        String truncatedBody = truncateJsonBodyIfNeeded(resBody, 1000);
        if (truncatedBody != null) logMessage.append("\nResponse Body: \n").append(truncatedBody);
        if (ex != null) logMessage.append("Exception : ").append(ex.getMessage());

        return logMessage.toString();
    }

    private LogLevel determineLogLevel(Log.LogBuilder logBuilder, Exception ex) {
        LogLevel finalLogLevel = logBuilder.build().getLogLevel();
        if (finalLogLevel == null) {
            finalLogLevel = LogLevel.INFO;
            logBuilder.logLevel(finalLogLevel).message("Request completed successfully");
        }
        return finalLogLevel;
    }

    private String paramsToJson(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        try {
            // ObjectMapper를 사용하여 Map을 JSON 문자열로 변환
            return objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            // 변환 중 에러 발생 시 에러 메시지 반환
            consoleLogger.error("Failed to serialize params to JSON", e);
            return "{\"error\":\"Failed to serialize params\"}";
        }
    }

    /**
     * 로그 레벨에 따라 콘솔에 로그를 출력
     */
    private void logToConsole(LogLevel level, String logMessage) {
        if (level == LogLevel.ERROR) {
            consoleLogger.error(logMessage);
        } else if (level == LogLevel.WARN) {
            consoleLogger.warn(logMessage);
        } else {
            consoleLogger.info(logMessage);
        }
    }

    ///로그 레벨에 따라 파일에 다른 형식의 로그를 출력

    private void logToFile(LogLevel level, int status, long duration, String detailedLog) {
        if (level == LogLevel.INFO) {
            String responseSummary = String.format("Request End   --- ID:[%s] Status:[%d] Duration:[%dms]",
                    MDC.get("traceId"), status, duration);
            fileLogger.info(responseSummary);
        } else {
            if (level == LogLevel.WARN) fileLogger.warn(detailedLog);
            else fileLogger.error(detailedLog);
        }
    }

    ///최종 Log 객체를 빌드하여 DB에 저장

    private void saveLogToDatabase(Log.LogBuilder logBuilder, HttpServletRequest req, int status, long duration) {
        String requestBody = getMaskedRequestBody(req);
        String requestParams = getMaskedRequestParamsAsJson(req);

        logBuilder.httpStatus(status)
                .durationMs(duration)
                .requestParams(requestParams)
                .requestBody(requestBody);

        logService.saveLog(logBuilder.build());
    }

    private String getMaskedRequestParamsAsJson(HttpServletRequest request) {
        Map<String, String> params = getMaskedRequestParams(request);
        if (params != null && !params.isEmpty()) {
            try {
                return objectMapper.writeValueAsString(params);
            } catch (JsonProcessingException e) {
                return "{\"error\":\"Failed to serialize params\"}";
            }
        }
        return null;
    }

    private Map<String, String> getMaskedRequestParams(HttpServletRequest request) {
        if (!request.getParameterNames().hasMoreElements()) return null;

        Map<String, String> params = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            params.put(paramName, request.getParameter(paramName));
        }

        for (String keyword : SENSITIVE_KEYWORDS) {
            if (params.containsKey(keyword)) {
                params.put(keyword, "****");
            }
        }
        return params;
    }

    private String getMaskedRequestBody(HttpServletRequest request) {
        if (request instanceof CustomHttpRequestWrapper) {
            byte[] bodyBytes = ((CustomHttpRequestWrapper) request).getRequestBody();
            if (bodyBytes != null && bodyBytes.length > 0) {
                String body = new String(bodyBytes);
                return maskSensitiveDataInJsonString(body);
            }
        }
        return null;
    }

    private String getResponseBody(HttpServletResponse response) {
        if (response instanceof CustomHttpResponseWrapper) {
            byte[] responseData = ((CustomHttpResponseWrapper) response).getResponseData();
            if (responseData != null && responseData.length > 0) {
                String rawBody = new String(responseData);
                try {
                    Object json = objectMapper.readValue(rawBody, Object.class);
                    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                } catch (Exception e) {
                    return rawBody;
                }
            }
        }
        return null;
    }

    private String maskSensitiveDataInJsonString(String jsonBody) {
        if (jsonBody == null || jsonBody.isBlank()) return jsonBody;

        String maskedBody = jsonBody;
        for (String keyword : SENSITIVE_KEYWORDS) {
            String regex = String.format("\"%s\"\\s*:\\s*\"(.*?)\"", keyword);
            String replacement = String.format("\"%s\":\"****\"", keyword);
            maskedBody = maskedBody.replaceAll(regex, replacement);
        }
        return maskedBody;
    }
    ///  너무 길면 잘라내는 메서드
    private String truncateJsonBodyIfNeeded(String jsonBody, int maxLength) {
        if (jsonBody == null || jsonBody.length() <= maxLength) {
            return jsonBody;
        }

        try {
            // Jackson의 JsonNode로 파싱하여 구조적으로 접근
            com.fasterxml.jackson.databind.JsonNode rootNode = objectMapper.readTree(jsonBody);

            // "content" 필드가 배열인 경우, 해당 부분만 요약 메시지로 교체
            if (rootNode.isObject() && rootNode.has("content") && rootNode.get("content").isArray()) {
                com.fasterxml.jackson.databind.node.ObjectNode objectNode = (com.fasterxml.jackson.databind.node.ObjectNode) rootNode;
                int arraySize = rootNode.get("content").size();
                objectNode.put("content", "[ " + arraySize + " items truncated... ]");
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
            }
        } catch (Exception e) {
            // JSON 파싱 실패 시, 그냥 앞에서부터 자르기
            return jsonBody.substring(0, maxLength) + "... (truncated)";
        }

        // "content" 배열이 없는 긴 JSON의 경우, 그냥 앞에서부터 자르기
        return jsonBody.substring(0, maxLength) + "... (truncated)";
    }

}