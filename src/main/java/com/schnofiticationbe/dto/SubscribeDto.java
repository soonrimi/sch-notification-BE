package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class SubscribeDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscribeCreateRequest {
        private String category;
        private String device; // 필수
        private List<String> includeKeywords;
        private List<String> excludeKeywords;
        private String department;
        private Integer year;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscribeUpdateRequest {
        private String category;
        private String device;
        private List<String> includeKeywords;
        private List<String> excludeKeywords;
        private String department;
        private Integer year;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscribeUpdateKeywordsRequest {
        private List<String> includeKeywords;
        private List<String> excludeKeywords;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscribeUpdateProfileRequest {
        private String device;
        private String department;
        private Integer year;
    }

    @Data
    public static class SubscribeResponse {
        private int id;
        private String category;
        private String device;
        private List<String> includeKeywords;
        private List<String> excludeKeywords;
        private String department;
        private Integer year;
        private LocalDateTime createdDate;

        public SubscribeResponse(Subscribe s) {
            this.id = s.getId();
            this.category = s.getCategory();
            this.device = s.getDevice();
            this.includeKeywords = s.getIncludeKeywords();
            this.excludeKeywords = s.getExcludeKeywords();
            this.department = s.getDepartment();
            this.year = s.getYear();
            this.createdDate = s.getCreatedDate();
        }
    }
}
