package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Admin;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

public interface InternalNoticeRepository extends JpaRepository<InternalNotice, Long>, JpaSpecificationExecutor<InternalNotice> {
     // 제목+내용 키워드로 찾기
     List<InternalNotice> findByWriter(Admin writer);
    // 카카오로 전송되지 않은 공지 리스트 반환
    List<InternalNotice> findBySentToKakaoFalse();

}
