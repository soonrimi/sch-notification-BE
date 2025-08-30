package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.FetchedNotice;
import com.schnofiticationbe.entity.Attachment;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class FetchedNoticeDto {
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CreateRequest {
        private String title;
        private String content;
        private String writer; // 크롤링된 작성자
        private String externalSourceUrl;
    }


    @Getter
    public static class AttachmentResponse {
        private String fileName;
        private String fileUrl;
        public AttachmentResponse(Attachment attachment) {
            this.fileName = attachment.getFileName();
            this.fileUrl = attachment.getFileUrl();
        }
    }

    @Getter
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private Timestamp createdAt;
        private int viewCount;
        private String writer;
        private String externalSourceUrl;
        private List<AttachmentResponse> attachments;

        public Response(FetchedNotice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.createdAt = notice.getCreatedAt();
            this.viewCount = notice.getViewCount();
            this.writer = notice.getWriter();
            this.externalSourceUrl = notice.getExternalSourceUrl();
            this.attachments = notice.getAttachments().stream()
                .map(AttachmentResponse::new)
                .collect(Collectors.toList());
        }
    }
}
