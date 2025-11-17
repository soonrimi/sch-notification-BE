package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.entity.KakaoRoomInfo;
import com.schnofiticationbe.entity.TargetYear;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class KakaoRoomInfoDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(requiredProperties = {"departmentId", "targetYear", "roomName"})
    public static class CreateKakaoRoomInfoRequest {
        private Long departmentId;
        private TargetYear targetYear;
        private String roomName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(requiredProperties = {"departmentId", "targetYear", "roomName"})
    public static class UpdateKakaoRoomInfoRequest {
        private Long departmentId;
        private TargetYear targetYear;
        private String roomName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(requiredProperties = {"id", "department", "targetYear", "roomName"})
    public static class KakaoRoomInfoResponse {
        private Long id;
        private Department department;
        private TargetYear targetYear;
        private String roomName;

        public KakaoRoomInfoResponse(KakaoRoomInfo kakaoRoomInfo) {
            this.id = kakaoRoomInfo.getId();
            this.department = kakaoRoomInfo.getDepartment();
            this.targetYear = kakaoRoomInfo.getTargetYear();
            this.roomName = kakaoRoomInfo.getRoomName();
        }
    }
}