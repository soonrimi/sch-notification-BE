package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.KeywordNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class KeywordDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private List<String> include;   // includeKeywords → include
        private List<String> exclude;   // excludeKeywords → exclude
        private String deviceId;        // device → deviceId
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private List<String> include;
        private List<String> exclude;
        private String deviceId;
    }

    @Data
    public static class Response {
        private Integer id;
        private List<String> include;
        private List<String> exclude;
        private String deviceId;
        private LocalDateTime createdDate;

        public Response(KeywordNotification k) {
            this.id = k.getId();
            this.include = k.getIncludeKeywords();
            this.exclude = k.getExcludeKeywords();
            this.deviceId = k.getDevice();
            this.createdDate = k.getCreatedDate();
        }
    }
}
