package com.schnofiticationbe.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class FcmService {

    @Value("${fcm.service-account-key-path:}")
    private String serviceAccountKeyPath;

    @Value("${fcm.service-account-key-classpath:keys/firebase-service-account.json}")
    private String serviceAccountKeyClasspath;

    @PostConstruct
    public void initialize() {
        try {
            // Firebase가 이미 초기화되어 있는지 확인
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options;

                // 환경 변수나 설정 파일에서 서비스 계정 키 경로를 지정한 경우
                if (serviceAccountKeyPath != null && !serviceAccountKeyPath.isBlank()) {
                    try (FileInputStream serviceAccount = new FileInputStream(serviceAccountKeyPath)) {
                        options = FirebaseOptions.builder()
                                .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(serviceAccount))
                                .build();
                    }
                } else {
                    // 클래스패스에서 서비스 계정 키 파일 읽기
                    ClassPathResource resource = new ClassPathResource(serviceAccountKeyClasspath);
                    if (!resource.exists()) {
                        log.warn("Firebase 서비스 계정 키 파일을 찾을 수 없습니다. 경로: {}", serviceAccountKeyClasspath);
                        log.warn("푸시 알림 기능이 비활성화됩니다. FCM 설정을 확인해주세요.");
                        return;
                    }

                    try (InputStream serviceAccount = resource.getInputStream()) {
                        options = FirebaseOptions.builder()
                                .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(serviceAccount))
                                .build();
                    }
                }

                FirebaseApp.initializeApp(options);
                log.info("Firebase Admin SDK 초기화 완료");
            } else {
                log.info("Firebase Admin SDK가 이미 초기화되어 있습니다.");
            }
        } catch (IOException e) {
            log.error("Firebase Admin SDK 초기화 실패", e);
        }
    }

    /**
     * 단일 디바이스에 푸시 알림 전송
     *
     * @param deviceToken FCM 디바이스 토큰
     * @param title       알림 제목
     * @param body        알림 본문
     * @param url         클릭 시 이동할 URL (선택사항)
     * @return 전송 성공 여부
     */
    public boolean sendPushNotification(String deviceToken, String title, String body, String url) {
        if (FirebaseApp.getApps().isEmpty()) {
            log.warn("Firebase가 초기화되지 않아 푸시 알림을 전송할 수 없습니다.");
            return false;
        }

        try {
            Message.Builder messageBuilder = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build());

            // URL이 있는 경우 데이터 페이로드에 추가
            if (url != null && !url.isBlank()) {
                messageBuilder.putData("url", url);
            }

            // 추가 데이터 (앱에서 처리할 수 있는 정보)
            messageBuilder.putData("click_action", "FLUTTER_NOTIFICATION_CLICK");

            Message message = messageBuilder.build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("푸시 알림 전송 성공. deviceToken: {}, response: {}", deviceToken, response);
            return true;
        } catch (FirebaseMessagingException e) {
            log.error("푸시 알림 전송 실패. deviceToken: {}, error: {}", deviceToken, e.getMessage(), e);
            
            // 토큰이 유효하지 않은 경우 (앱 삭제, 토큰 만료 등)
            if (e.getErrorCode().equals("invalid-argument") || 
                e.getErrorCode().equals("registration-token-not-registered")) {
                log.warn("유효하지 않은 디바이스 토큰입니다. deviceToken: {}", deviceToken);
            }
            return false;
        } catch (Exception e) {
            log.error("푸시 알림 전송 중 예외 발생. deviceToken: {}", deviceToken, e);
            return false;
        }
    }

    /**
     * 여러 디바이스에 동시에 푸시 알림 전송 (멀티캐스트)
     *
     * @param deviceTokens FCM 디바이스 토큰 리스트
     * @param title        알림 제목
     * @param body         알림 본문
     * @param url          클릭 시 이동할 URL (선택사항)
     * @return 성공적으로 전송된 알림 수
     */
    public int sendMulticastPushNotification(java.util.List<String> deviceTokens, String title, String body, String url) {
        if (FirebaseApp.getApps().isEmpty()) {
            log.warn("Firebase가 초기화되지 않아 푸시 알림을 전송할 수 없습니다.");
            return 0;
        }

        if (deviceTokens == null || deviceTokens.isEmpty()) {
            return 0;
        }

        try {
            MulticastMessage.Builder messageBuilder = MulticastMessage.builder()
                    .addAllTokens(deviceTokens)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build());

            // URL이 있는 경우 데이터 페이로드에 추가
            if (url != null && !url.isBlank()) {
                messageBuilder.putData("url", url);
            }

            messageBuilder.putData("click_action", "FLUTTER_NOTIFICATION_CLICK");

            MulticastMessage message = messageBuilder.build();

            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            
            int successCount = response.getSuccessCount();
            int failureCount = response.getFailureCount();
            
            log.info("멀티캐스트 푸시 알림 전송 완료. 성공: {}, 실패: {}", successCount, failureCount);

            // 실패한 토큰들 로깅
            if (failureCount > 0) {
                response.getResponses().forEach((responseItem, index) -> {
                    if (!responseItem.isSuccessful()) {
                        log.warn("푸시 알림 전송 실패. deviceToken: {}, error: {}", 
                                deviceTokens.get(index), responseItem.getException().getMessage());
                    }
                });
            }

            return successCount;
        } catch (Exception e) {
            log.error("멀티캐스트 푸시 알림 전송 중 예외 발생", e);
            return 0;
        }
    }
}

