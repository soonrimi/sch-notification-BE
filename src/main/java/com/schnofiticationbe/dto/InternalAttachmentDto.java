package com.schnofiticationbe.dto;

import lombok.Builder; // Lombok Builder
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.schnofiticationbe.entity.Attachment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternalAttachmentDto {
    private String fileName;
    private String fileUrl;

    // 엔티티를 DTO로 변환하는 생성자 (조회용)
    public InternalAttachmentDto(Attachment attachment) {
        this.fileName = attachment.getFileName();
        this.fileUrl = attachment.getFileUrl();
    }
}