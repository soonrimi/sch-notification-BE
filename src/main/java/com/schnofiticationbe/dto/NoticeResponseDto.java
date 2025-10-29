package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NoticeResponseDto {

    // 첨부파일 응답 DTO는 공통으로 사용 가능 (filename, fileUrl만 필요하므로)
    @Getter
    @Schema(requiredProperties = {"fileName", "fileUrl"})
    public static class AttachmentResponse {
        private String fileName;
        private String fileUrl;

        // 생성자는 파일 정보만 받도록 변경
        public AttachmentResponse(String fileName, String fileUrl) {
            this.fileName = fileName;
            this.fileUrl = fileUrl;
        }
    }

    // 모든 게시물을 포괄하는 공통 응답 DTO
    @Getter
    @Schema(requiredProperties = {"id", "title", "content", "writer", "createdAt", "category", "viewCount", "noticeType",
            "externalSourceUrl", "source", "crawlAttachments",
            "targetYear", "targetDeptNames", "internalAttachments"})
    public static class NoticeResponse {
        private Long id;
        private String title;
        private String content;
        private String writer; // Admin (Internal), String (Crawl) -> 편의상 String으로 변환
        private Timestamp createdAt;
        private Category category;
        private int viewCount;
        private NoticeType noticeType; // 게시물 출처 (CRAWL, INTERNAL)

        // CrawlPosts 전용 필드 (InternalNotice일 경우 null)
        private String externalSourceUrl;
        private String source;
        private List<CrawlAttachment> crawlAttachments;

        // InternalNotice 전용 필드 (CrawlPosts일 경우 null)
        private String targetYear;
        private Set<String> targetDeptNames;
        private List<InternalAttachment> internalAttachments;



        // CrawlPosts -> NoticeResponse 변환 생성자
        public NoticeResponse(CrawlPosts crawlPosts) {
            this.id = crawlPosts.getId();
            this.title = crawlPosts.getTitle();
            this.content = crawlPosts.getContent();
            this.writer=crawlPosts.getWriter();
            this.createdAt = crawlPosts.getCreatedAt();
            this.category = crawlPosts.getCategory();
            this.viewCount = crawlPosts.getViewCount();
            this.noticeType = NoticeType.CRAWL;

            this.externalSourceUrl = crawlPosts.getExternalSourceUrl();
            this.source = crawlPosts.getSource();

            this.crawlAttachments = crawlPosts.getCrawlAttachments().stream()
                    .map(crawlAttachment -> new CrawlAttachment(crawlAttachment.getFileName(), crawlAttachment.getFileUrl()))
                    .collect(Collectors.toList());
        }

        // InternalNotice -> NoticeResponse 변환 생성자
        public NoticeResponse(InternalNotice internalNotice) {
            this.id = internalNotice.getId();
            this.title = internalNotice.getTitle();
            this.content = internalNotice.getContent();
            this.writer = internalNotice.getWriter() != null ? internalNotice.getWriter().getUserId() : null; // Admin 객체에서 ID 가져오기
            this.createdAt = internalNotice.getCreatedAt();
            this.category = internalNotice.getCategory();
            this.viewCount = internalNotice.getViewCount();
            this.noticeType = NoticeType.INTERNAL;

            this.targetYear = String.valueOf(internalNotice.getTargetYear());
            this.targetDeptNames = internalNotice.getTargetDept() != null ?
                    internalNotice.getTargetDept().stream().map(Department::getName).collect(Collectors.toSet()) : null;

            this.internalAttachments = internalNotice.getInternalAttachment().stream()
                    .map(internalAttachments -> new InternalAttachment(internalAttachments.getFileName(), internalAttachments.getFileUrl()))
                    .collect(Collectors.toList());
        }
    }
}