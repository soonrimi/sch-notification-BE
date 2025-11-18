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
        private String device;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscribeUpdateRequest {  // ✅ 이름 컨트롤러랑 통일
        private String category;
        private String device;
    }

    @Data
    public static class SubscribeResponse {
        private int id;
        private String category;
        private String device;
        private LocalDateTime createdDate;

        public SubscribeResponse(Subscribe subscribe) {  // ✅ 생성자 이름 수정
            this.id = subscribe.getId();
            this.category = subscribe.getCategory();
            this.device = subscribe.getDevice();
            this.createdDate = subscribe.getCreatedDate();
        }
    }
}
