package conconccc.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "admin")
public class Admin {
    // getter/setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // PK

    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String username;   // 아이디

    @Setter
    @Column(nullable = false)
    private String passwordHash;   // 비밀번호 해시

    @Setter
    @Column(nullable = false, length = 100)
    private String name;   // 이름

    @Setter
    @Column(length = 50)
    private String role;   // 소속

}