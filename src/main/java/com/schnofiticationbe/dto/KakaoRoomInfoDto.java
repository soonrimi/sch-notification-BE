package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.KakaoRoomInfo;
import lombok.*;

public class KakaoRoomInfoDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateKakaoRoomInfoRequest {
        private Long lessonId;
        private String targetYear;
        private String roomName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateKakaoRoomInfoRequest {
        private Long lessonId;
        private String targetYear;
        private String roomName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class KakaoRoomInfoResponse {
        private Long id;
        private Long lessonId;
        private String targetYear;
        private String roomName;

        public KakaoRoomInfoResponse(KakaoRoomInfo kakaoRoomInfo) {
            this.id = kakaoRoomInfo.getId();
            this.lessonId = kakaoRoomInfo.getLessonId();
            this.targetYear = kakaoRoomInfo.getTargetYear().toString();
            this.roomName = kakaoRoomInfo.getRoomName();
        }
    }
}