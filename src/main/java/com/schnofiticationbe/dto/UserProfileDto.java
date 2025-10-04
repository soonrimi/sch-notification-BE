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
        private String device; // 선택적
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String department;
        private Integer grade;
        private String device;
    }

    @Data
    public static class Response {
        private Integer id;
        private String department;
        private Integer grade;
        private String device;
        private LocalDateTime createdDate;

        public Response(UserProfile p) {
            this.id = p.getId();
            this.department = p.getDepartment();
            this.grade = p.getGrade();
            this.device = p.getDevice();
            this.createdDate = p.getCreatedDate();
        }
    }
}
