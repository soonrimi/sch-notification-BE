package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Attachment;
import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    public static class CreateInternalNoticeRequest {
        private String title;
        private String content;
        private InternalNotice.TargetYear targetYear;
        private long targetDept;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UpdateInternalNoticeRequest {
        private String title;
        private String content;
        private String writer;
        private String externalSourceUrl;
    }


    @Getter
    public static class InternalNoticeAttachmentResponse {
        private String fileName;
        private String fileUrl;

        public InternalNoticeAttachmentResponse(Attachment attachment) {
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
    public static class InternalNoticeListResponse {
        private long id;
        private String title;
        private String content;
        private Timestamp createdAt;
        private int viewCount;
        private String writerName;
        private InternalNotice.TargetYear targetYear;
        private Department targetDept;
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
            this.attachments = notice.getAttachments().stream()
                    .map(attachment -> new InternalNoticeAttachmentResponse(attachment))
                    .collect(Collectors.toList());
        }
    }
}
