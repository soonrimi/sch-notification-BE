package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.entity.Attachment;
import com.schnofiticationbe.entity.Notice;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class CrawlPostDto {
    @Getter @Setter
    public static class CreateRequest {
        private String title;
        private String content;
        private String targetYear;
        private String targetDept;
        private Admin writer;
    }

    @Getter @Setter
    public static class UpdateRequest {
        private String title;
        private String content;
        private String targetYear;
        private String targetDept;
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
    public static class CrawlPostsResponse {
        private Long id;
        private String title;
        private String content;
        private Timestamp createdAt;
        private int viewCount;
        private List<AttachmentResponse> attachments;

        public CrawlPostsResponse(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            //this.createdAt = notice.getCreatedAt();
            this.viewCount = notice.getViewCount();
            this.attachments = notice.getAttachments().stream()
                .map(attachment -> new AttachmentResponse(attachment))
                .collect(Collectors.toList());
        }
    }
}
