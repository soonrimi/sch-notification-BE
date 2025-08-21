package conconccc.schnofiticationbe.service;

import conconccc.schnofiticationbe.dto.BoardDto; // 수정된 DTO 임포트
import conconccc.schnofiticationbe.entity.Board;
import conconccc.schnofiticationbe.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * 게시물 생성
     */
    @Transactional
    public BoardDto.Response createBoard(BoardDto.Request requestDto) {
        Board board = requestDto.toEntity();
        Board savedBoard = boardRepository.save(board);
        return new BoardDto.Response(savedBoard);
    }

    /**
     * 전체 게시물 조회
     */
    @Transactional(readOnly = true)
    public Page<BoardDto.Response> getAllBoards(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(pageable);

        return boardPage.map(BoardDto.Response::new);
    }

    /**
     * 단일 게시물 조회
     */
    @Transactional(readOnly = true)
    public BoardDto.Response getBoardById(Long id) {
        Board board = findBoardById(id);
        return new BoardDto.Response(board);
    }

    /**
     * 게시물 수정
     */
    @Transactional
    public BoardDto.Response updateBoard(Long id, BoardDto.Request requestDto) {
        Board board = findBoardById(id);

        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());

        return new BoardDto.Response(board);
    }

    /**
     * 게시물 삭제
     */
    @Transactional
    public void deleteBoard(Long id) {
        Board board = findBoardById(id);
        boardRepository.delete(board);
    }

    /**
     * 중복 코드 제거를 위한 헬퍼 메서드
     */
    private Board findBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시물을 찾을 수 없습니다: " + id));
    }
}