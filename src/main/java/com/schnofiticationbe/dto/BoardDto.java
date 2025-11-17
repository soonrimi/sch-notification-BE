package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Attachment;
import com.schnofiticationbe.entity.Board;
import com.schnofiticationbe.entity.BoardAttachment;
import com.schnofiticationbe.entity.NoticeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class BoardDto {
    @Getter @Setter
    @Schema(requiredProperties = {"title", "content", "attachments", "noticeType"})
    public static class CreateRequest {
        private String title;
        private String content;
        private List<BoardAttachment> attachments;
        private NoticeType noticeType;
    }
    @Schema(requiredProperties = {"title", "content"})
    @Getter @Setter
    public static class UpdateRequest {
        private String title;
        private String content;
    }

    @Getter
    @Schema(requiredProperties = {"fileName", "fileUrl"})
    public static class AttachmentResponse {
        private String fileName;
        private String fileUrl;

        public AttachmentResponse(BoardAttachment attachment) {
            this.fileName = attachment.getFileName();
            this.fileUrl = attachment.getFileUrl();
        }
    }

    @Getter
    @Schema(requiredProperties = {"id", "title", "content", "createdAt", "attachments", "noticeType"})
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private Timestamp createdAt;
        private List<AttachmentResponse> attachments;
        private NoticeType noticeType;

        public Response(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.createdAt = board.getCreatedAt();
            this.attachments = board.getAttachments().stream()
                    .map(AttachmentResponse::new)
                    .collect(Collectors.toList());
            this.noticeType=NoticeType.BOARD;
        }
    }
}
