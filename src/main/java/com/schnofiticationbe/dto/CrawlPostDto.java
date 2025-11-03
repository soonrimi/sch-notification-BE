package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class CrawlPostDto {
    @Getter @Setter
    @Schema(requiredProperties = {"title", "content", "targetYear", "targetDept", "writer", "noticeType"})
    public static class CreateRequest {
        private String title;
        private String content;
        private String targetYear;
        private String targetDept;
        private Admin writer;
        private NoticeType noticeType;
    }

    @Getter @Setter
    @Schema(requiredProperties = {"title", "content", "targetYear", "targetDept"})
    public static class UpdateRequest {
        private String title;
        private String content;
        private String targetYear;
        private String targetDept;
    }

    @Getter
    @Schema(requiredProperties = {"id", "fileName", "fileUrl"})
    public static class AttachmentResponse {
        private Long id;
        private String fileName;
        private String fileUrl;

        public AttachmentResponse(CrawlAttachment crawlAttachment) {
            this.id = crawlAttachment.getId();
            this.fileName = crawlAttachment.getFileName();
            this.fileUrl = crawlAttachment.getFileUrl();
        }
    }

    @Getter
    @Schema(requiredProperties = {"id", "title", "content", "writer", "createdAt", "category", "viewCount", "noticeType", "attachments"})
    public static class CrawlPostsResponse {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private Timestamp createdAt;
        private Category category;
        private Integer viewCount;
        private NoticeType noticeType;
        private List<AttachmentResponse> attachments;

        public CrawlPostsResponse(CrawlPosts crawlPosts) {
            this.id = crawlPosts.getId();
            this.title = crawlPosts.getTitle();
            this.content = crawlPosts.getContent();
            this.category = crawlPosts.getCategory();
            this.writer = crawlPosts.getWriter();
            this.createdAt = crawlPosts.getCreatedAt();
            this.viewCount = crawlPosts.getViewCount();
            this.noticeType = NoticeType.CRAWL;
            this.attachments = crawlPosts.getCrawlAttachments().stream()
                .map(crawlAttachment -> new AttachmentResponse(crawlAttachment))
                .collect(Collectors.toList());
        }
    }
}
