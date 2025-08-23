package conconccc.schnofiticationbe.dto;

import conconccc.schnofiticationbe.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// 이 클래스는 네임스페이스 역할만 하므로 직접 인스턴스화하지 않도록 private 생성자를 추가합니다.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDto {

    @Getter
    @NoArgsConstructor
    @Schema(name = "BoardDto.Request", description = "건의사항 게시물 생성 및 수정 요청 DTO")
    public static class Request {

        @Schema(description = "건의사항 게시물 제목", example = "서버 점검 문의")
        private String title;

        @Schema(description = "건의사항 게시물 내용", example = "서버 점검은 언제까지 진행되나요?")
        private String content;

        // Service에서 DTO를 Entity로 변환할 때 사용할 메서드
        public Board toEntity() {
            return Board.builder()
                    .title(this.title)
                    .content(this.content)
                    .createdAt(LocalDate.now())
                    .build();
        }
    }

    @Getter
    @Schema(name = "BoardDto.Response", description = "건의사항 게시물 응답 DTO")
    public static class Response {

        @Schema(description = "건의사항 게시물 고유 ID", example = "1")
        private final Long id;

        @Schema(description = "건의사항 게시물 제목", example = "캘린더 안떠요")
        private final String title;

        @Schema(description = "건의사항 게시물 내용", example = "캘린더 아예 안나옴")
        private final String content;

        @Schema(description = "건의사항 작성일시")
        private final LocalDate createdAt;

        public Response(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.createdAt = board.getCreatedAt();
        }
    }
}