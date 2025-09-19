package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Attachment;
import com.schnofiticationbe.entity.Board;
import com.schnofiticationbe.entity.BoardAttachment;
import com.schnofiticationbe.entity.NoticeType;
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
        private List<BoardAttachment> attachments;
        private NoticeType noticeType;
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

        public AttachmentResponse(BoardAttachment attachment) {
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
        private NoticeType noticeType;

        public Response(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.createdAt = board.getCreatedAt();
            this.attachments = board.getBoardAttachments().stream()
                    .map(AttachmentResponse::new)
                    .collect(Collectors.toList());
            this.noticeType=NoticeType.BOARD;
        }
    }
}
