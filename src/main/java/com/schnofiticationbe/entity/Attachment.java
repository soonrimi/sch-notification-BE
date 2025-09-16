package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Schema(requiredProperties = {"id", "fileName", "fileUrl", "name"})

public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName; // 첨부파일 이름

    @Column(nullable = false)
    private String fileUrl;  // 첨부파일 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id" )
    private Notice notice;

    public Attachment(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

}
