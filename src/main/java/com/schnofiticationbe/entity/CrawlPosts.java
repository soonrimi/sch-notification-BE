package com.schnofiticationbe.entity;

import com.schnofiticationbe.Utils.JsonStringListConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CRAWL")
@Schema(requiredProperties = {"id", "title", "content", "createdAt", "view_count", "writer", "category","externalSourceUrl", "source", "CrawlAttachments"})
public class CrawlPosts extends Notice {
    @Column(nullable = true)
    private String writer; // 크롤링된 공지의 작성자 (String)

    @Column(nullable = true)
    private String externalSourceUrl; // 크롤링 원본 URL

    @Column()
    private String source; // 공지 출처 (예: 학교 홈페이지, 특정 게시판 등)

    @OneToMany(mappedBy = "crawlPosts", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CrawlAttachment> CrawlAttachments = new ArrayList<>();

    public void addAttachment(CrawlAttachment attachment) {
        this.CrawlAttachments.add(attachment);
        attachment.setCrawlPosts(this);
    }

    @Convert(converter = JsonStringListConverter.class)
    @Column(name = "content_images", columnDefinition = "LONGTEXT")
    private List<String> contentImages;
}
