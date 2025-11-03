package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Subscribe;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(requiredProperties = {"category", "device"})
    public static class CreateRequest {
        private String category;
        private String device;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(requiredProperties = {"category", "device"})
    public static class UpdateRequest {
        private String category;
        private String device;
    }

    @Data
    @Schema(requiredProperties = {"id", "category", "device", "createdDate"})
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
