package com.schnofiticationbe.entity;

import com.schnofiticationbe.dto.KakaoRoomInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Schema(requiredProperties = {"id", "department", "targetYear", "roomName"})
public class KakaoRoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne()
    @JoinColumn()
    private Department department;

    @Setter
    @Column(nullable = false)
    private TargetYear targetYear;

    @Setter
    @Column(nullable = false)
    private String roomName;

    public static KakaoRoomInfo from(Department department, KakaoRoomInfoDto.CreateKakaoRoomInfoRequest dto) {
        KakaoRoomInfo entity = new KakaoRoomInfo();
        entity.department = department;
        entity.targetYear = dto.getTargetYear();
        entity.roomName = dto.getRoomName();
        return entity;
    }
}
