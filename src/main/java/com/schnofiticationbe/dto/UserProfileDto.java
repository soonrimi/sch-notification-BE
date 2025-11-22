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
            this.deviceId = p.getDevice();   // 엔티티의 device 필드와 매핑
            this.createdDate = p.getCreatedDate();
        }
    }
}
