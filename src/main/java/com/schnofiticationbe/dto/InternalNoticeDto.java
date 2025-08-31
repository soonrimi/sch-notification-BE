package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Attachment;
import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.entity.InternalNotice;
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
    public static class CreateRequest {
        private String title;
        private String content;
        private InternalNotice.TargetYear targetYear;
        private String targetDept;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UpdateRequest {
        private String title;
        private String content;
        private String writer;
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

    // 응답용 DTO
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private Timestamp createdAt;
        private int viewCount;
        private String writerName;
        private InternalNotice.TargetYear targetYear;
        private String targetDept;
        private List<AttachmentResponse> attachments;

        public Response(com.schnofiticationbe.entity.InternalNotice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            //this.createdAt = notice.getCreatedAt();
            this.viewCount = notice.getViewCount();
            this.writerName = notice.getWriter().getName();
            this.targetYear = notice.getTargetYear();
            this.targetDept = notice.getTargetDept();
            this.attachments = notice.getAttachments().stream()
                    .map(attachment -> new AttachmentResponse(attachment))
                    .collect(Collectors.toList());
        }
    }
}
