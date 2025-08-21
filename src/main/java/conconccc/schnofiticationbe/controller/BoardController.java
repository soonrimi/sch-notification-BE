package conconccc.schnofiticationbe.controller;

import conconccc.schnofiticationbe.dto.BoardDto; // 수정된 DTO 임포트
import conconccc.schnofiticationbe.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "Board API", description = "건의사항 게시판 CRUD API")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @Operation(summary = "건의사항 게시물 생성", description = "새로운 건의사항 게시물을 등록합니다.")
    public ResponseEntity<BoardDto.Response> createBoard(@RequestBody BoardDto.Request requestDto) {
        BoardDto.Response responseDto = boardService.createBoard(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "모든 건의사항 게시물 페이징 조회", description = "모든 건의사항 게시물 목록을 페이징하여 조회합니다.")
    public ResponseEntity<Page<BoardDto.Response>> getAllBoards(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<BoardDto.Response> boards = boardService.getAllBoards(pageable);
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 건의사항 게시물 조회", description = "ID를 이용하여 특정 건의사항 게시물을 조회합니다.")
    public ResponseEntity<BoardDto.Response> getBoardById(
            @Parameter(description = "조회할 게시물 ID", required = true) @PathVariable Long id) {
        BoardDto.Response board = boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

    @PutMapping("/{id}")
    @Operation(summary = "건의사항 게시물 수정", description = "ID를 이용하여 기존 건의사항 게시물을 수정합니다.")
    public ResponseEntity<BoardDto.Response> updateBoard(
            @Parameter(description = "수정할 게시물 ID", required = true) @PathVariable Long id,
            @RequestBody BoardDto.Request requestDto) {
        BoardDto.Response updatedBoard = boardService.updateBoard(id, requestDto);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "건의사항 게시물 삭제", description = "ID를 이용하여 건의사항 게시물을 삭제합니다.")
    public ResponseEntity<Void> deleteBoard(
            @Parameter(description = "삭제할 게시물 ID", required = true) @PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}