package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.*;
import com.schnofiticationbe.entity.NoticeType;
import com.schnofiticationbe.entity.TargetYear;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Notice 관련 DTO들을 모아놓은 컨테이너 클래스입니다.
 */
public class NoticeDto {

    /**
     * 공지 목록 조회 시 사용되는 간결한 응답 DTO입니다.
     */
    @Getter
    @Schema(requiredProperties = {"id", "title", "createdAt", "viewCount", "writer", "content", "noticeType", "categoryName"})
    public static class ListResponse {
        private final Long id;
        private final String title;
        private final Timestamp createdAt;
        private final Integer viewCount;
        private final String writer;
        private final String content;
        private final NoticeType noticeType;
        private final String categoryName;

        public ListResponse(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.createdAt = notice.getCreatedAt();
            this.viewCount = notice.getViewCount();
            this.categoryName = notice.getCategory() != null ? notice.getCategory().getDescription() : "미분류";
            this.content = notice.getContent();
            this.writer = notice.getWriterName();
            this.noticeType = notice.getNoticeTypeEnum();
        }
    }

    /**
     * 단일 공지 상세 조회 시 사용되는 모든 정보를 포함하는 응답 DTO입니다.
     */
    @Getter
    @Schema(requiredProperties = {"id", "title", "content", "writer", "createdAt", "viewCount", "categoryName", "noticeType", "targetYear", "targetDept", "attachments"})
    public static class DetailResponse {
        private final Long id;
        private final String title;
        private final String content;
        private final String writer;
        private final Timestamp createdAt;
        private final Integer viewCount;
        private final String categoryName;
        private final NoticeType noticeType;
        private final TargetYear targetYear; // InternalNotice 전용
        private final Set<Department> targetDept; // InternalNotice 전용
        private final List<AttachmentResponse> attachments;
        private final List<String> contentImages; // CrawlPosts 전용

        public DetailResponse(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.createdAt = notice.getCreatedAt();
            this.viewCount = notice.getViewCount();
            this.categoryName = notice.getCategory() != null ? notice.getCategory().getDescription() : "미분류";

            this.attachments = notice.getAttachments().stream()
                    .map(AttachmentResponse::new)
                    .collect(Collectors.toList());
            if (notice instanceof InternalNotice internalNotice) {
                if (internalNotice.getWriter() != null) {
                    this.writer = internalNotice.getWriter().getName();
                } else {
                    this.writer = "작성자 정보 없음";
                }
                this.noticeType = NoticeType.INTERNAL;
                this.targetYear = internalNotice.getTargetYear();
                this.targetDept = internalNotice.getTargetDept();
                this.contentImages = Collections.emptyList();

            } else if (notice instanceof CrawlPosts crawlPosts) {
                this.writer = crawlPosts.getWriter();
                this.noticeType = NoticeType.CRAWL;
                this.targetYear = null;
                this.targetDept = Collections.emptySet();
                this.contentImages = crawlPosts.getContentImages();

            } else {
                this.writer = "알 수 없음";
                this.noticeType = null;
                this.targetYear = null;
                this.targetDept = Collections.emptySet();
                this.contentImages = Collections.emptyList();
            }
        }
    }

    /**
     * 첨부파일 정보를 담는 공통 DTO입니다.
     */
    @Getter
    @Schema(requiredProperties = {"id", "fileName", "fileUrl"})
    public static class AttachmentResponse {
        private final Long id;
        private final String fileName;
        private final String fileUrl;

        public AttachmentResponse(Attachment attachment) {
            this.id = attachment.getId();
            this.fileName = attachment.getFileName();
            this.fileUrl = attachment.getFileUrl();
        }
    }

}

