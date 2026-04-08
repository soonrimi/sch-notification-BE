package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class SubscribeDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscribeCreateRequest {
        private String category;
        // FE 요구사항: deviceId 라는 이름으로 받기
        private String deviceId;
        // 선택적으로 보낼 수 있게
        private Boolean subscribed;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscribeUpdateRequest {
        private String category;
        private String deviceId;
        private Boolean subscribed;
    }

    @Data
    public static class SubscribeResponse {
        private int id;
        private String category;
        private String deviceId;   // FE 기준 이름
        private boolean subscribed;
        private LocalDateTime createdAt; // createdDate → createdAt 으로 변경

        public SubscribeResponse(Subscribe subscribe) {
            this.id = subscribe.getId();
            this.category = subscribe.getCategory();
            // 엔티티의 device 필드를 응답의 deviceId 로 매핑
            this.deviceId = subscribe.getDevice();
            this.subscribed = subscribe.isSubscribed();
            // 엔티티의 createdDate → 응답 필드 createdAt
            this.createdAt = subscribe.getCreatedDate();
        }
    }
}
