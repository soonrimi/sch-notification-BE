package com.schnofiticationbe.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FcmService {

    @Value("${fcm.server-key:}")
    private String serverKey;

    @Value("${fcm.api-url:https://fcm.googleapis.com/fcm/send}")
    private String fcmApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendPushNotification(String deviceToken, String title, String body) {
        if (serverKey == null || serverKey.isBlank()) {
            log.warn("FCM 서버 키가 설정되지 않았습니다.");
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "key=" + serverKey);

            Map<String, Object> notification = new HashMap<>();
            notification.put("title", title);
            notification.put("body", body);

            Map<String, Object> message = new HashMap<>();
            message.put("to", deviceToken);
            message.put("notification", notification);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(message, headers);
            restTemplate.postForEntity(fcmApiUrl, request, String.class);

            log.info("푸시 알림 전송 성공: deviceToken={}", deviceToken);
        } catch (Exception e) {
            log.error("푸시 알림 전송 실패: {}", e.getMessage(), e);
        }
    }
}

