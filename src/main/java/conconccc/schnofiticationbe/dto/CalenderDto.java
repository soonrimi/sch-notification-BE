package conconccc.schnofiticationbe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CalenderDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @Schema(name = "calenderDto.CreateRequest", description = "캘린더 일정 생성 요청 DTO")
    public static class CreateRequest {
        @Size(min = 1, max = 100)
        private String title;
        @Size(max=1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
        private String content;
        private String startDate;
        private String endDate;
        private String type;

    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @Schema(name = "calenderDto.UpdateRequest", description = "캘린더 일정 수정 요청 DTO")
    public static class UpdateRequest {
        @Size(min = 1, max = 100)
        private String title;
        @Size(max=1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
        private String content;
        private String startDate;
        private String endDate;
        private String type;
    }
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    @Schema(name = "CalenderDto.Response", description = "캘린더 일정 응답 DTO")
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String startDate;
        private String endDate;
        private String type;
    }
}
