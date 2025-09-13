package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.entity.KakaoRoomInfo;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KakaoRoomInfoRepository extends JpaRepository<KakaoRoomInfo,Long> {
    List<KakaoRoomInfo> findByDepartmentAndTargetYear(Department department, InternalNotice.TargetYear targetYear);

    List<KakaoRoomInfo> findAllByDepartmentId(Long departmentId, Limit limit);
}
