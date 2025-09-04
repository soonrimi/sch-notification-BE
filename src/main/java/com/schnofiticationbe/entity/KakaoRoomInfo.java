package com.schnofiticationbe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class KakaoRoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private Long lessonId;

    @Setter
    @Column(nullable = false)
    private InternalNotice.TargetYear targetYear;

    @Setter
    @Column(nullable = false, unique = true)
    private String roomName;
}
