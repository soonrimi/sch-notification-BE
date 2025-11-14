package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("SELECT a FROM Attachment a WHERE a.notice.id = :noticeId")
    List<Attachment> findByNoticeId(@Param("noticeId") Long noticeId);
}
