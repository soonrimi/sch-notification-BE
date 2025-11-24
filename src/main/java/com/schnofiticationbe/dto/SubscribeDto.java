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
        private LocalDateTime createdDate;

        public SubscribeResponse(Subscribe subscribe) {
            this.id = subscribe.getId();
            this.category = subscribe.getCategory();
            this.deviceId = subscribe.getDevice();  // 엔티티의 device와 매핑
            this.subscribed = subscribe.isSubscribed();
            this.createdDate = subscribe.getCreatedDate();
        }
    }
}
