package com.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attachment")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id" )
    private Board board;

    public Attachment(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public void setNotice(Notice notice) {
        this.setNotice(notice);
    }

    public void setBoard(Board board) {
        this.setBoard(board);
    }
}
