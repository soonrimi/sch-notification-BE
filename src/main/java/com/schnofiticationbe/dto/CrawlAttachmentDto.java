package com.schnofiticationbe.dto;

import com.schnofiticationbe.entity.Attachment;

public class CrawlAttachmentDto {
    private String fileName;
    private String fileUrl;

    // 엔티티를 DTO로 변환하는 생성자 (조회용)
    public CrawlAttachmentDto(Attachment attachment) {
        this.fileName = attachment.getFileName();
        this.fileUrl = attachment.getFileUrl();
    }
}
