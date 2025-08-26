package com.schnofiticationbe.service;

import com.schnofiticationbe.Utils.StoreAttachment;
import com.schnofiticationbe.dto.BoardDto;
import com.schnofiticationbe.entity.Attachment;
import com.schnofiticationbe.entity.Board;
import com.schnofiticationbe.repository.AttachmentRepository;
import com.schnofiticationbe.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final AttachmentRepository attachmentRepository;
    private final StoreAttachment storeAttachment;

    public BoardDto.Response createBoard(BoardDto.CreateRequest req, List<MultipartFile> files) {

        Board board = new Board();
        board.setTitle(req.getTitle());
        board.setContent(req.getContent());
        board.setCreatedAt(Timestamp.from(Instant.now()));

        Board savedBoard = boardRepository.save(board);

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileUrl = storeAttachment.saveFile(file);

                    Attachment attachment = new Attachment();
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileUrl(fileUrl);
                    attachment.setBoard(savedBoard);
                    savedBoard.addAttachment(attachment);

                    attachmentRepository.save(attachment);
                }
            }
        }

        return new BoardDto.Response(savedBoard);
    }

    public Page<BoardDto.Response> getAllBoards(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardDto.Response::new);
    }

    public BoardDto.Response getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        return new BoardDto.Response(board);
    }
    /**
     * 학과명 중복 제거 조회
     */
    @Transactional
    public List<String> getDistinctTitles() {
        return boardRepository.findDistinctTitleBy();
    }

}
