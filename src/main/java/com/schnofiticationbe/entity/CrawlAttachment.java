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
@DiscriminatorValue("CRAWL")
@Schema(requiredProperties = {"id", "fileName", "fileUrl" ,"crawlPosts"})
public class CrawlAttachment extends Attachment{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crawl_posts_id")
    private CrawlPosts crawlPosts;

    public CrawlAttachment(String fileName, String fileUrl) {
        super(fileName, fileUrl);
    }

}
