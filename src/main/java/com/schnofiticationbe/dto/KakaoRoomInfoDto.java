package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.entity.KakaoRoomInfo;
import lombok.*;

public class KakaoRoomInfoDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateKakaoRoomInfoRequest {
        private Long departmentId;
        private InternalNotice.TargetYear targetYear;
        private String roomName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateKakaoRoomInfoRequest {
        private Long departmentId;
        private InternalNotice.TargetYear targetYear;
        private String roomName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class KakaoRoomInfoResponse {
        private Long id;
        private Department department;
        private InternalNotice.TargetYear targetYear;
        private String roomName;

        public KakaoRoomInfoResponse(KakaoRoomInfo kakaoRoomInfo) {
            this.id = kakaoRoomInfo.getId();
            this.department = kakaoRoomInfo.getDepartment();
            this.targetYear = kakaoRoomInfo.getTargetYear();
            this.roomName = kakaoRoomInfo.getRoomName();
        }
    }
}