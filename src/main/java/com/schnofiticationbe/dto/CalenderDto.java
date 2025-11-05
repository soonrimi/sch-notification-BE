package com.schnofiticationbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CalenderDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @Schema(name = "calenderDto.CreateRequest", description = "캘린더 일정 생성 요청 DTO", requiredProperties = {"title", "startDate", "endDate", "type"})
    public static class CreateRequest {
        @Size(min = 1, max = 100)
        private String title;
        private String startDate;
        private String endDate;
        private String type;

    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @Schema(name = "calenderDto.UpdateRequest", description = "캘린더 일정 수정 요청 DTO", requiredProperties = {"title", "startDate", "endDate", "type"})
    public static class UpdateRequest {
        @Size(min = 1, max = 100)
        private String title;
        private String startDate;
        private String endDate;
        private String type;
    }
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @Schema(name = "CalenderDto.Response", description = "캘린더 일정 응답 DTO", requiredProperties = {"id", "title", "startDate", "endDate", "type"})
    public static class Response {
        private Long id;
        private String title;
        private String startDate;
        private String endDate;
        private String type;
    }
}
