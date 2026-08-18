package com.schnofiticationbe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "device")
public class Device {

    /**
     * 서버에서 생성하는 고유 deviceId (UUID 등)
     */
    @Id
    @Column(name = "device_id", length = 64)
    private String deviceId;

    /**
     * FCM / APNs 등에서 발급하는 디바이스 토큰
     */
    @Column(name = "device_token", nullable = false, length = 4096)
    private String deviceToken;

    /**
     * ANDROID / IOS 등
     */
    @Column(name = "os", length = 20)
    private String os;

    /**
     * 앱 버전
     */
    @Column(name = "app_version", length = 50)
    private String appVersion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

