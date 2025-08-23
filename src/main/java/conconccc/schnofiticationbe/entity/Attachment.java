package conconccc.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName; // 첨부파일 이름

    @Column(nullable = false)
    private String fileUrl;  // 첨부파일 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_post_id", foreignKey = @ForeignKey(name = "fk_attachment_base_post", value = ConstraintMode.CONSTRAINT)) // 외래키 조건 명시
    private BasePost basePost;

    public Attachment(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public void setBasePost(BasePost basePost) {
        this.basePost = basePost;
    }
}
