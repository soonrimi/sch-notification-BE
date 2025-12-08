package com.schnofiticationbe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

public class NotificationDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewPostRequest {
        // 크롤러나 게시글 DB의 id (문자열/숫자 상관 없음, 서버에서는 매칭 용도로만 사용)
        private String postId;

        // 예: "전체", "홍보", "취업", "학과" 등
        private String category;

        // 공지 제목
        private String title;

        // 공지 내용(요약 또는 전체)
        private String content;

        // 상세 페이지로 이동할 수 있는 URL
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendSummary {
        // 카테고리 구독으로 알림 대상이 된 Subscribe 개수
        private int categoryMatchCount;

        // 키워드 구독으로 알림 대상이 된 KeywordNotification 개수
        private int keywordMatchCount;

        // 최종적으로 알림을 보낸 디바이스 수 (중복 제거)
        private int totalDeviceCount;

        // 어떤 디바이스들에게 보냈는지 (디버깅용, FE에서 안 써도 됨)
        private Set<String> targetDevices;
    }
}
