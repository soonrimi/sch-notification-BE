package com.schnofiticationbe.service;

import org.springframework.transaction.annotation.Transactional;
import com.schnofiticationbe.dto.KakaoMessageDto;
import com.schnofiticationbe.dto.InternalNoticeDto.InternalNoticeListResponse;
import com.schnofiticationbe.dto.KakaoRoomInfoDto.KakaoRoomInfoResponse;
import com.schnofiticationbe.dto.UnreadNoticeWithRoomResponse;
import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.entity.TargetYear;
import com.schnofiticationbe.repository.KakaoRoomInfoRepository;
import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.repository.InternalNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KakaoMessageService {
    private final InternalNoticeRepository internalNoticeRepository;
    private final KakaoRoomInfoRepository kakaoRoomInfoRepository;

    private static final Logger log = LoggerFactory.getLogger(KakaoMessageService.class);

    // 카카오로 전송되지 않은(읽지 않은) 공지 + 해당 공지의 방 정보 리스트 반환
    public List<UnreadNoticeWithRoomResponse> getUnreadNoticesWithRooms() {
        List<InternalNotice> unreadNotices = internalNoticeRepository.findBySentToKakaoFalse();
        return unreadNotices.stream().map(notice -> {
            InternalNoticeListResponse noticeDto = new InternalNoticeListResponse(notice);
            // 여러 부서에 보낼 수 있으므로, 각 부서별로 방을 모두 모은다
            List<KakaoRoomInfoResponse> rooms = notice.getTargetDept().stream()
                .flatMap(dept -> kakaoRoomInfoRepository.findByDepartmentAndTargetYear(dept, notice.getTargetYear()).stream())
                .map(KakaoRoomInfoResponse::new)
                .toList();
            return UnreadNoticeWithRoomResponse.builder()
                .notice(noticeDto)
                .kakaoRooms(rooms)
                .build();
        }).toList();
    }

    // 공지 전송 완료 처리
    @Transactional
    public void markNoticeAsSentToKakao(Long noticeId) {
        log.info("[markNoticeAsSentToKakao] 요청: noticeId={}", noticeId);
        try {
            InternalNotice notice = internalNoticeRepository.findById(noticeId)
                    .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. id=" + noticeId));
            notice.setSentToKakao(true);
            internalNoticeRepository.save(notice);
            log.info("[markNoticeAsSentToKakao] 성공: noticeId={}", noticeId);
        } catch (Exception e) {
            log.error("[markNoticeAsSentToKakao] 실패: noticeId={}, error={}", noticeId, e.getMessage(), e);
            throw e;
        }
    }
}
