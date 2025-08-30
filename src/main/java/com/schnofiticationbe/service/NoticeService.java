package com.schnofiticationbe.service;

import com.schnofiticationbe.Utils.StoreAttachment;
import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.entity.Attachment;
import com.schnofiticationbe.entity.Notice;
import com.schnofiticationbe.repository.AdminRepository;
import com.schnofiticationbe.repository.AttachmentRepository;
import com.schnofiticationbe.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    // 전체 공지 조회
    public List<NoticeDto.Response> getAllNotices() {
        return noticeRepository.findAll()
                .stream()
                .map(NoticeDto.Response::new)
                .toList();
    }

    // 단일 공지 조회
    public NoticeDto.Response getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));

        notice.setViewCount(notice.getViewCount() + 1); // 조회수 증가
        Notice saved = noticeRepository.save(notice);
        return new NoticeDto.Response(saved);
    }

    public void deleteNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항을 찾을 수 없습니다."));

        noticeRepository.delete(notice);
    }

    public List<Notice> searchNotices(String keyword) {
        return noticeRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }
}
