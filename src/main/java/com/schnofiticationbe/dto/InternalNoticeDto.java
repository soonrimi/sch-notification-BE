package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(nullable = false)
public class InternalNoticeDto {
    private Long id;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Integer viewCount;
    private Admin writer;
    private String targetYear;
    private String targetDept;
    private Boolean sentToKakao;
    private NoticeType noticeType;
    private List<Attachment> attachments;

    // 생성 요청용 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(requiredProperties = {"title", "content", "category", "targetYear", "targetDepartmentIds"})
    public static class CreateInternalNoticeRequest {
        private String title;
        private String content;
        private Admin writer;
        private Category category;
        private TargetYear targetYear;
        private Set<Long> targetDepartmentIds;
    }

    @Getter
    @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UpdateInternalNoticeRequest {
        private String title;
        private String content;
        private Admin writer;
        private String externalSourceUrl;
    }



    // 응답용 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(requiredProperties = {"id", "title", "content", "createdAt", "viewCount", "writerName", "category", "targetYear", "targetDept", "attachments"})
    public static class InternalNoticeListResponse {
        private long id;
        private String title;
        private String content;
        private Admin writer;
        private Timestamp createdAt;
        private Integer viewCount;
        private String writerName;
        private Category category;
        private TargetYear targetYear;
        private Set<Department> targetDept;
        private NoticeType noticeType;
        private List<NoticeDto.AttachmentResponse> attachments;

        public InternalNoticeListResponse(InternalNotice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.writer = notice.getWriter();
            this.createdAt = notice.getCreatedAt();
            this.viewCount = notice.getViewCount();
            this.writerName = notice.getWriter().getName();
            this.targetYear = notice.getTargetYear();
            this.targetDept = notice.getTargetDept();
            this.category = notice.getCategory();
            this.noticeType = NoticeType.INTERNAL;
            this.attachments = notice.getAttachments().stream()
                    .map(NoticeDto.AttachmentResponse::new) // 공통 DTO 생성자 사용
                    .collect(Collectors.toList());

        }
    }
}
