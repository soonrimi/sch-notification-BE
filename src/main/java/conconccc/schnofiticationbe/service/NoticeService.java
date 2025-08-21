package conconccc.schnofiticationbe.service;

import conconccc.schnofiticationbe.dto.NoticeDto;
import conconccc.schnofiticationbe.entity.Admin;
import conconccc.schnofiticationbe.entity.Attachment;
import conconccc.schnofiticationbe.entity.Notice;
import conconccc.schnofiticationbe.repository.AdminRepository;
import conconccc.schnofiticationbe.repository.AttachmentRepository;
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
    private final AttachmentRepository attachmentRepository;

    // 수정사항 : 여러 개 파일 받도록 수정
    public NoticeDto.Response createNotice(Long adminId, NoticeDto.CreateRequest req, List<MultipartFile> files) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자가 존재하지 않습니다."));

        // 1. 공지 먼저 저장
        Notice notice = new Notice();
        notice.setTitle(req.getTitle());
        notice.setAuthor(admin.getName());
        notice.setContent(req.getContent());
        notice.setCreatedAt(LocalDateTime.now());
        notice.setViewCount(0);
        notice.setSource("admin");
        notice.setTargetYear(req.getTargetYear());
        notice.setTargetDept(req.getTargetDept());

        Notice savedNotice = noticeRepository.save(notice);

        // 2. 첨부파일 같이 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileUrl = saveFile(file);

                    Attachment attachment = new Attachment();
                    attachment.setFileName(file.getOriginalFilename());
                    attachment.setFileUrl(fileUrl);
                    attachment.setNotice(savedNotice); // FK 연결
                    savedNotice.getAttachments().add(attachment);

                    attachmentRepository.save(attachment);
                }
            }
        }

        return new NoticeDto.Response(savedNotice);
    }

    // 악성파일 거르기
    private boolean isAllowedExtension(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".png") ||
                lower.endsWith(".pdf") || lower.endsWith(".docx") ||
                lower.endsWith(".hwp") || lower.endsWith(".zip") ||
                lower.endsWith(".xlsx") || lower.endsWith(".jpeg");
    }

    private String saveFile(MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();

            if (!isAllowedExtension(originalName)) {
                throw new RuntimeException("허용되지 않은 파일 형식입니다: " + originalName);
            }
            // 프로젝트 루트 기준 절대 경로
            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

            // uploads 폴더 만약 없으면 자동 추가
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir + fileName);
            file.transferTo(dest);

            // DB에는 URL 경로 저장 (WebConfig와 매핑되는 /uploads/**)
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
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
}
