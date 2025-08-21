package conconccc.schnofiticationbe.dto;

import conconccc.schnofiticationbe.entity.Attachment;
import conconccc.schnofiticationbe.entity.Notice;
import lombok.Getter;
import lombok.Setter;


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
        private String source;
    }

    @Getter
    public static class AttachmentResponse {
        private String fileName;
        private String fileUrl;

        public AttachmentResponse(Attachment a) {
            this.fileName = a.getFileName();
            this.fileUrl = a.getFileUrl();
        }

        private String fileUrl;
    }

    @Getter
    public static class Response {
        private Long id;
        private String title;
        private String author;
        private String content;
        private String targetYear;
        private String targetDept;
        private String source;
        private String createdAt;
        private int viewCount;
        private List<AttachmentResponse> attachments;


        public Response(Notice n) {
            this.id = n.getId();
            this.title = n.getTitle();
            this.author = n.getAuthor();
            this.content = n.getContent();
            this.targetYear = n.getTargetYear();
            this.targetDept = n.getTargetDept();
            this.source = n.getSource();
            this.viewCount = n.getViewCount();

            // LocalDateTime → String 변환
            this.createdAt = n.getCreatedAt() != null
                    ? n.getCreatedAt().toString()
                    : null;

            this.attachments = n.getAttachments()
                    .stream()
                    .map(AttachmentResponse::new)
                    .collect(Collectors.toList());

        }
    }
}
