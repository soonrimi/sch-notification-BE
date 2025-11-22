package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserProfileDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private String department;
        private Integer grade;
        // JSON 필드 이름: deviceId
        private String deviceId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String department;
        private Integer grade;
        private String deviceId;
    }

    @Data
    public static class Response {
        private Integer id;
        private String department;
        private Integer grade;
        private String deviceId;
        private LocalDateTime createdDate;

        public Response(UserProfile p) {
            this.id = p.getId();
            this.department = p.getDepartment();
            this.grade = p.getGrade();
            // 엔티티 필드 device → 응답 필드 deviceId 로 매핑
            this.deviceId = p.getDevice();
            this.createdDate = p.getCreatedDate();
        }
    }
}
