package com.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CrawlPosts")
public class CrawlPosts extends Notice {
    @Column(nullable = false)
    private String writer; // 크롤링된 공지의 작성자 (String)

    @Column(nullable = true)
    private String externalSourceUrl; // 크롤링 원본 URL

    @Column()
    private String source; // 공지 출처 (예: 학교 홈페이지, 특정 게시판 등)

}
