package com.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("NOTICE")
public class Notice{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(nullable = false)
    private String author;

    private int viewCount; // 조회수
    private String source; // 출처 구분: "admin", "school", "club", "kakao"

    private String targetYear;  // "1,2,3,전체"
    private String targetDept;  // "컴퓨터공학과, 전체"

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();


    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setNotice(this);
    }



}
