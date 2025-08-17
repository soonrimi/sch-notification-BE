package conconccc.schnofiticationbe.service;

import conconccc.schnofiticationbe.dto.NoticeDto;
import conconccc.schnofiticationbe.entity.Admin;
import conconccc.schnofiticationbe.entity.Notice;
import conconccc.schnofiticationbe.repository.AdminRepository;
import conconccc.schnofiticationbe.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;

    public NoticeDto.Response createNotice(Long adminId, NoticeDto.CreateRequest req, MultipartFile file) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자가 존재하지 않습니다."));

        String fileUrl = null;
        if (file != null && !file.isEmpty()) {
            fileUrl = saveFile(file);
        }

        Notice notice = new Notice();
        notice.setTitle(req.getTitle());
        notice.setAuthor(admin.getName()); // 관리자 이름 자동 저장
        notice.setContent(req.getContent());
        notice.setCreatedAt(LocalDateTime.now());
        notice.setViewCount(0);
        notice.setFileUrl(fileUrl);
        notice.setSource("admin");
        notice.setTargetYear(req.getTargetYear());
        notice.setTargetDept(req.getTargetDept());

        Notice saved = noticeRepository.save(notice);
        return new NoticeDto.Response(saved);
    }

    private String saveFile(MultipartFile file) {
        try {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            // DB에는 파일 경로 대신 URL 저장 (실제 서비스에서는 CDN이나 S3 주소)
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    // 전체 공지 조회
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    // 단일 공지 조회
    public Notice getNotice(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        notice.setViewCount(notice.getViewCount() + 1); // 조회수 증가
        return noticeRepository.save(notice);
    }
}
