package com.schnofiticationbe.service;

import com.schnofiticationbe.Utils.StoreAttachment;
import com.schnofiticationbe.dto.BoardDto;
import com.schnofiticationbe.entity.Attachment;
import com.schnofiticationbe.entity.Board;
import com.schnofiticationbe.entity.BoardAttachment;
import com.schnofiticationbe.repository.AttachmentRepository;
import com.schnofiticationbe.repository.BoardRepository;
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

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileUrl = storeAttachment.saveFile(file);
                    String originalFilename = file.getOriginalFilename();

                    BoardAttachment attachment = new BoardAttachment(originalFilename, fileUrl);

                    board.addAttachment(attachment);
                }
            }
        }

        Board savedBoard = boardRepository.save(board);
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

}
