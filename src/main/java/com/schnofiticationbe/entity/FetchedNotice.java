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
@DiscriminatorValue("FETCHED")
public class FetchedNotice extends Notice {
    @Column(nullable = false)
    private String writer; // 크롤링된 공지의 작성자 (String)

    @Column(nullable = true)
    private String externalSourceUrl; // 크롤링 원본 URL

    private String source; // 출처 구분: "admin", "school", "club", "kakao"

    // FetchedNotice만의 추가 필드가 있다면 여기에 작성
}
