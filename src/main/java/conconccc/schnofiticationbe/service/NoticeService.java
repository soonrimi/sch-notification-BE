package conconccc.schnofiticationbe.service;

import conconccc.schnofiticationbe.Utils.StoreAttachment;
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

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;
    private final AttachmentRepository attachmentRepository;
    private final StoreAttachment storeAttachment;

    // 수정사항 : 여러 개 파일 받도록 수정
    public NoticeDto.Response createNotice(Long adminId, NoticeDto.CreateRequest req, List<MultipartFile> files) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자가 존재하지 않습니다."));

        // 1. 공지 먼저 저장
        Notice notice = new Notice();
        notice.setTitle(req.getTitle());
        notice.setContent(req.getContent());
        notice.setAuthor(admin.getName());
        notice.setCreatedAt(Timestamp.from(Instant.now()));
        notice.setViewCount(0);
        notice.setSource("admin");
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
                    attachment.setBasePost(savedNotice); // FK 연결
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
