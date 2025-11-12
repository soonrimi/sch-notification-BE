package com.schnofiticationbe.entity;

import com.schnofiticationbe.Utils.JsonStringListConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CRAWL")
@Schema(requiredProperties = {"writer", "externalSourceUrl", "source", "contentImages"})
public class CrawlPosts extends Notice {
    @Column(nullable = true)
    private String writer; // 크롤링된 공지의 작성자 (String)

    @Column(nullable = true)
    private String externalSourceUrl; // 크롤링 원본 URL

    @Column()
    private String source; // 공지 출처 (예: 학교 홈페이지, 특정 게시판 등)

    @Convert(converter = JsonStringListConverter.class)
    @Column(name = "content_images", columnDefinition = "LONGTEXT")
    private List<String> contentImages;

    @Override
    public String getWriterName() {
        return this.writer != null ? this.writer : "정보 없음";
    }

    @Override
    public NoticeType getNoticeTypeEnum() {
        return NoticeType.CRAWL;
    }
}
