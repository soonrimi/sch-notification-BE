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
    private int viewCount;
    private Admin writer;
    private String targetYear;
    private String targetDept;
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
        private Category category;
        private TargetYear targetYear;
        private Set<Long> targetDepartmentIds;
    }

    @Getter
    @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UpdateInternalNoticeRequest {
        private String title;
        private String content;
        private String writer;
        private String externalSourceUrl;
    }


    @Getter
    public static class InternalNoticeAttachmentResponse {
        private Long id;
        private String fileName;
        private String fileUrl;

        public InternalNoticeAttachmentResponse(Attachment attachment) {
            this.id = attachment.getId();
            this.fileName = attachment.getFileName();
            this.fileUrl = attachment.getFileUrl();
        }
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
        private Timestamp createdAt;
        private int viewCount;
        private String writerName;
        private Category category;
        private TargetYear targetYear;
        private Set<Department> targetDept;
        private List<InternalNoticeAttachmentResponse> attachments;

        public InternalNoticeListResponse(com.schnofiticationbe.entity.InternalNotice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.createdAt = notice.getCreatedAt();
            this.viewCount = notice.getViewCount();
            this.writerName = notice.getWriter().getName();
            this.targetYear = notice.getTargetYear();
            this.targetDept = notice.getTargetDept();
            this.category = notice.getCategory();
            this.attachments = notice.getAttachments().stream()
                    .map(attachment -> new InternalNoticeAttachmentResponse(attachment))
                    .collect(Collectors.toList());
        }
    }
}
