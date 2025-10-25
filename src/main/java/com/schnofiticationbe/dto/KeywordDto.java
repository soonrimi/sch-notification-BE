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
        private List<String> includeKeywords;
        private List<String> excludeKeywords;
        private String device;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private List<String> includeKeywords;
        private List<String> excludeKeywords;
        private String device;
    }

    @Data
    public static class Response {
        private Integer id;
        private List<String> includeKeywords;
        private List<String> excludeKeywords;
        private String device;
        private LocalDateTime createdDate;

        public Response(KeywordNotification k) {
            this.id = k.getId();
            this.includeKeywords = k.getIncludeKeywords();
            this.excludeKeywords = k.getExcludeKeywords();
            this.device = k.getDevice();
            this.createdDate = k.getCreatedDate();
        }
    }
}
