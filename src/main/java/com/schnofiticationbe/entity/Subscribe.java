package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Schema(requiredProperties = {"id", "category", "device", "subscribed", "createdDate"})
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // 아이디

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String device; // 실제 DB 컬럼은 그대로 'device'

    // ✅ FE에서 필요로 하는 subscribed 플래그
    @Column(nullable = false)
    private boolean subscribed = true;

    @CreationTimestamp
    private LocalDateTime createdDate;
}
