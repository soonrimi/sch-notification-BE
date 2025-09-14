
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


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kakao/notices")
public class KakaoMessageController {
    private final KakaoMessageService kakaoMessageService;

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
}
