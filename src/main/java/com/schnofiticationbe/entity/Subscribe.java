package conconccc.schnofiticationbe.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity

public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //아이디

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String device;

    @CreationTimestamp
    private LocalDateTime createdDate;



}
