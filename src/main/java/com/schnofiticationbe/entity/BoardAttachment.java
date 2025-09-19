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
@DiscriminatorValue("BOARD")
@Schema(name = "BoardAttachment", description = "건의사항 첨부파일 엔티티", requiredProperties = {"id", "fileName", "fileUrl" ,"board"})
public class BoardAttachment extends Attachment{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardAttachment(String fileName, String fileUrl) {
        super(fileName, fileUrl);
    }
}
