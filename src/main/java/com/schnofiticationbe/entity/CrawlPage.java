package com.schnofiticationbe.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * crawl_pages 테이블과 매핑되는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "crawl_pages")
@Getter
@Setter
@NoArgsConstructor
public class CrawlPage {

    /**
     * 기본 키, 자동 증가
     * id INT NOT NULL AUTO_INCREMENT
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 게시판 이름 (예: 학사공지)
     * title VARCHAR(255) NOT NULL
     */
    @Column(nullable = false, length = 255)
    private String title;

    /**
     * 게시판 절대 경로 URL
     * absolute_url VARCHAR(512) NOT NULL
     * DB의 스네이크 케이스(snake_case) 컬럼명을 자바의 카멜 케이스(camelCase) 필드명에 매핑합니다.
     */
    @Column(name = "absolute_url", nullable = false, length = 512, unique = true)
    private String absoluteUrl;

    /**
     * 게시판 카테고리 (예: 학사)
     * category VARCHAR(100) NULL
     */
    @Column(length = 100)
    private String category;
}

