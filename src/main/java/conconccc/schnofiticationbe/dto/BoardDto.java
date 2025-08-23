package conconccc.schnofiticationbe.dto;

import conconccc.schnofiticationbe.entity.Attachment;
import conconccc.schnofiticationbe.entity.Board;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class BoardDto {
    @Getter @Setter
    public static class CreateRequest {
        private String title;
        private String content;
    }

    @Getter @Setter
    public static class UpdateRequest {
        private String title;
        private String content;
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
        private Timestamp createdAt;
        private List<AttachmentResponse> attachments;

        public Response(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.createdAt = board.getCreatedAt();
            this.attachments = board.getAttachments().stream()
                .map(AttachmentResponse::new)
                .collect(Collectors.toList());
        }
    }
}
