package conconccc.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("NOTICE")
public class Notice extends BasePost {
    @Column(nullable = false)
    private String author;

    private int viewCount; // 조회수
    private String source; // 출처 구분: "admin", "school", "club", "kakao"

    private String targetYear;  // "1,2,3,전체"
    private String targetDept;  // "컴퓨터공학과, 전체"

}
