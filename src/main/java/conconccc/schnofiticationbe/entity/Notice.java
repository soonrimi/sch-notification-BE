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

    @ManyToOne
    @JoinColumn(name = "writer", nullable = false)
    private Admin writer; // 작성자

    private String targetYear;  // "1,2,3,전체"
    private String targetDept;  // "컴퓨터공학과, 전체"

}
