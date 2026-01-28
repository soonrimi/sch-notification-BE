package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class DeviceDto {

    /**
     * 클라이언트 → 서버 디바이스 등록 요청
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        private String deviceToken;
        private String os;          // ANDROID / IOS
        private String appVersion;  // 예: 1.0.0
    }

    /**
     * 서버 → 클라이언트 디바이스 등록 응답
     */
    @Data
    public static class RegisterResponse {
        private String deviceId;
        private String deviceToken;
        private String os;
        private String appVersion;
        private LocalDateTime createdAt;

        public RegisterResponse(Device device) {
            this.deviceId = device.getDeviceId();
            this.deviceToken = device.getDeviceToken();
            this.os = device.getOs();
            this.appVersion = device.getAppVersion();
            this.createdAt = device.getCreatedAt();
        }
    }
}

