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
    private final AdminRepository adminRepository;
    private final AttachmentRepository attachmentRepository;
    private final StoreAttachment storeAttachment;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public NoticeDto.Response createNotice(String jwtToken, NoticeDto.CreateRequest req, List<MultipartFile> files) {
        String username = getUsernameFromToken(jwtToken);
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("관리자가 존재하지 않습니다."));

        // 1. 공지 먼저 저장
        Notice notice = new Notice();
        notice.setTitle(req.getTitle());
        notice.setContent(req.getContent());
        notice.setWriter(admin);
        notice.setCreatedAt(Timestamp.from(Instant.now()));
        notice.setViewCount(0);
        notice.setTargetYear(req.getTargetYear());
        notice.setTargetDept(req.getTargetDept());

        Notice savedNotice = noticeRepository.save(notice);

        // 2. 첨부파일 같이 저장
        if (files != null && !files.isEmpty()) {
            System.out.println("업로드된 파일 개수: " + files.size());
            for (MultipartFile f : files) {
                System.out.println("파일 이름: " + f.getOriginalFilename());
            }

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileUrl = storeAttachment.saveFile(file);

                    Attachment attachment = new Attachment();
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileUrl(fileUrl);
                    attachment.setNotice(savedNotice);
                    savedNotice.getAttachments().add(attachment);

                    attachmentRepository.save(attachment);
                }
            }
        }

        return new NoticeDto.Response(savedNotice);
    }



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
