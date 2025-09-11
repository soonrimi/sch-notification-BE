package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.entity.KakaoRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KakaoRoomInfoRepository extends JpaRepository<KakaoRoomInfo,Long> {
    List<KakaoRoomInfo> findByLessonIdAndTargetYear(Long lessonId, InternalNotice.TargetYear  targetYear);
    List<KakaoRoomInfo> findAllByLessonId(Long lessonId);
}
