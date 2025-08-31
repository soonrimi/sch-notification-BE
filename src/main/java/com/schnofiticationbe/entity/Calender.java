package com.schnofiticationbe.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "calendar")
@Schema(name = "Calendar", description = "캘린더 엔티티")
public class Calender {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Schema(description = "캘린더 이벤트 ID", example = "1")
    private Long id;

    @Column(name = "title")
    @Schema(description = "캘린더 이벤트 제목", example = "학교 행사")
    private String title;

    @Column(name = "start_date", nullable = false, columnDefinition = "DATE")
    @Schema(description = "캘린더 이벤트 시작 날짜", example = "2023-10-01")
    private String startDate;

    @Column(name = "end_date", columnDefinition = "DATE")
    @Schema(description = "캘린더 이벤트 종료 날짜", example = "2023-10-02")
    private String endDate;


    @Column(name = "type")
    @Schema(description = "캘린더 이벤트 타입", example = "holyday")
    private String type;


}
