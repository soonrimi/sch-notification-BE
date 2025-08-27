package conconccc.schnofiticationbe.dto;

import conconccc.schnofiticationbe.entity.Admin;
import conconccc.schnofiticationbe.entity.Attachment;
import conconccc.schnofiticationbe.entity.Notice;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class NoticeDto {
    @Getter @Setter
    public static class CreateRequest {
        private String title;
        private String content;
        private String targetYear;
        private String targetDept;
        private String author;
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
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String author;
        private Timestamp createdAt;
        private int viewCount;
        private String targetYear;
        private String targetDept;
        private Admin writer;
        private List<AttachmentResponse> attachments;

        public Response(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.author = notice.getAuthor();
            this.createdAt = notice.getCreatedAt();
            this.viewCount = notice.getViewCount();
            this.targetYear = notice.getTargetYear();
            this.targetDept = notice.getTargetDept();
            this.writer = notice.getWriter();
            this.attachments = notice.getAttachments().stream()
                .map(attachment -> new AttachmentResponse(attachment))
                .collect(Collectors.toList());
        }
    }
}
