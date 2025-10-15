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
    public static class CreateRequest {
        private String category;
        private String device;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String category;
        private String device;
    }

    @Data
    public static class Response {
        private int id;
        private String category;
        private String device;
        private LocalDateTime createdDate;

        public Response(Subscribe subscribe) {
            this.id = subscribe.getId();
            this.category = subscribe.getCategory();
            this.device = subscribe.getDevice();
            this.createdDate = subscribe.getCreatedDate();
        }
    }
}
