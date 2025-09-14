package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternalNoticeRepository extends JpaRepository<InternalNotice, Long> {
    List<InternalNotice> findByWriter(Admin writer); // 제목+내용 키워드로 찾기

    // 카카오로 전송되지 않은 공지 리스트 반환
    List<InternalNotice> findBySentToKakaoFalse();
}
