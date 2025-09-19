package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("BOARD")
@Schema(name = "Board", description = "건의사항 엔티티", requiredProperties = {"id", "title", "content", "createdAt", "boardAttachments"})

public class Board{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardAttachment> boardAttachments = new ArrayList<>();

    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    // == 연관관계 편의 메소드 == //
    public void addAttachment(BoardAttachment attachment) {
        this.boardAttachments.add(attachment);
        attachment.setBoard(this); // Attachment에도 Board 정보를 설정합니다. (양방향)
    }
}