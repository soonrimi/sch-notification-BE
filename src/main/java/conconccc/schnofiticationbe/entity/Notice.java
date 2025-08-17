package conconccc.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 제목
    private String author; // 작성자 (관리자 이름, 학교측 이름, 크롤링 원문 작성자)

    @Column(columnDefinition = "TEXT")
    private String content; // 내용

    private LocalDateTime createdAt; // 등록일
    private int viewCount; // 조회수
    private String fileUrl; // 첨부파일
    private String source; // 출처 구분: "admin", "school", "club", "kakao"

    private String targetYear;  // "1,2,3,전체"
    private String targetDept;  // "컴퓨터공학과, 전체"
}

