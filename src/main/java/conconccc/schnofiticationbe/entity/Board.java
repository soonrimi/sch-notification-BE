package conconccc.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "board")
@Schema(name = "Board", description = "건의사항 엔티티")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "건의사항 게시물 고유 ID",example = "1")
    private Long id;

    @Column(name = "title")
    @Schema(description = "건의사항 게시물 제목", example = "캘린더 안떠요")
    private String title;

    @Column(name="content")
    @Schema(description = "건의사항 게시물 내용", example = "캘린더 아예 안나옴")
    private String content;

    @Column(name = "created_at")
    @Schema(description = "건의사항 작성일시")
    private LocalDate createdAt;


}
