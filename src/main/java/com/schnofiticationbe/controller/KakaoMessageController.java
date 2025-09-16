
package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.UnreadNoticeWithRoomResponse;
import com.schnofiticationbe.service.KakaoMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.schnofiticationbe.repository.AttachmentRepository;
import com.schnofiticationbe.entity.Attachment;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao/notices")
public class KakaoMessageController {
    private final KakaoMessageService kakaoMessageService;
    private final AttachmentRepository attachmentRepository;

    // 카카오로 전송되지 않은(읽지 않은) 공지 리스트 반환
    @GetMapping("/unread")
    public ResponseEntity<List<UnreadNoticeWithRoomResponse>> getUnreadNoticesForKakao() {
        List<UnreadNoticeWithRoomResponse> unreadNotices = kakaoMessageService.getUnreadNoticesWithRooms();
        return ResponseEntity.ok(unreadNotices);
    }
    // 공지 전송 완료 처리
    @PostMapping("/{id}/sent")
    public ResponseEntity<Void> markNoticeAsSentToKakao(@PathVariable Long id) {
        kakaoMessageService.markNoticeAsSentToKakao(id);
        return ResponseEntity.ok().build();
    }
    // 첨부파일 다운로드
    @GetMapping("/attachment/download/{id}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long id) throws Exception {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("첨부파일을 찾을 수 없습니다. id=" + id));
    String absolutePath = System.getProperty("user.dir") + attachment.getFileUrl();
    Resource resource = new UrlResource("file:" + absolutePath);
        String contentDisposition = "attachment; filename=\"" + attachment.getFileName() + "\"";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
