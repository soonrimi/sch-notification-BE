package com.schnofiticationbe.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiscordService {

    private final RestTemplate restTemplate;

    @Value("${discord.webhook.url}") // application.properties에 URL 등록
    private String webhookUrl;

    private void sendToDiscord(Map<String, Object> requestMap) {
        org.springframework.retry.support.RetryTemplate retryTemplate = org.springframework.retry.support.RetryTemplate.builder()
                .maxAttempts(3)
                .fixedBackoff(1000)
                .build();

        try {
            retryTemplate.execute(context -> {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);
                restTemplate.postForEntity(webhookUrl, entity, String.class);
                return null;
            });
        } catch (Exception e) {
            log.error("디스코드 전송 최종 실패 (재시도 초과): {}", e.getMessage(), e);
        }
    }

    // 2. [조건 1] 서버 에러 알림 (비동기)
    @Async
    public void sendErrorAlert(String title, String errorMessage, String path, String traceId) {
        Map<String, Object> requestMap = new HashMap<>();
        
        // title에서 상태 코드를 추출하여 강조 표시 (예: "⚠️ Bad Gateway (502)..." -> "502")
        String contentHeader = "🚨 **서버 오류 발생**";
        if (title != null) {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\((\\d{3})\\)").matcher(title);
            if (matcher.find()) {
                contentHeader = "🚨 **서버 오류 발생 (" + matcher.group(1) + ")**";
            }
        }

        requestMap.put("content", contentHeader);

        Map<String, String> embed = new HashMap<>();
        embed.put("title", title);
        String description = String.format("**Ref ID:** `%s`\n**Path:** %s\n**Error:** %s",
                traceId, path, errorMessage);
        embed.put("description", description);
        embed.put("color", "16711680"); // Red

        requestMap.put("embeds", List.of(embed));
        sendToDiscord(requestMap);
    }

    // 3. [조건 2] 가입 알림 (비동기)
    @Async
    public void sendJoinAlert(String userId, String department, String role) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("content", "🎉 **새로운 관리자 가입!**");

        Map<String, String> embed = new HashMap<>();
        embed.put("title", "가입 정보");
        // 마크다운 문법 사용 가능
        embed.put("description",
                String.format("**ID:** %s\n**학과:** %s\n**권한:** %s", userId, department, role));
        embed.put("color", "65280"); // Green

        requestMap.put("embeds", List.of(embed));
        sendToDiscord(requestMap);
    }

    // 4. [조건 3] 일반 알림 (비동기)
    @Async
    public void sendInfoAlert(String title, String description) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("content", "📢 **알림**");

        Map<String, String> embed = new HashMap<>();
        embed.put("title", title);
        embed.put("description", description);
        embed.put("color", "3447003"); // Blue color

        requestMap.put("embeds", List.of(embed));
        sendToDiscord(requestMap);
    }
}