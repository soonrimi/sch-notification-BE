package com.schnofiticationbe.repository;

import com.schnofiticationbe.dto.NoticeDto;
import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.entity.Notice;
import com.schnofiticationbe.entity.TargetYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long>, JpaSpecificationExecutor<Notice> {
    @Query("SELECT n FROM Notice n ORDER BY n.createdAt DESC")
    Page<Notice> findCombinedNoticesOrderByCreatedAtDesc(Pageable pageable);
    Page<Notice> findByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);

    Page<Notice> findByCategory(Category category, Pageable pageable);

    Page<Notice> findByTitleContainingOrContentContaining(String keyword, String keyword1, Pageable pageable);

    Page<Notice> findByIdInOrderByCreatedAtDesc(List<Long> idlist, Pageable pageable);

    @Query(value = "SELECT n FROM Notice n " +
            "WHERE (:ids IS NULL OR n.id IN :ids) " +
            "AND (n.title LIKE %:searchKeyword% OR n.content LIKE %:searchKeyword%) " +
            "ORDER BY n.createdAt DESC")
    Page<Notice> findByIdAndTitleContainingOrContentContainingOrderByCreatedAtDescCustom(List<Long> ids,String searchKeyword,Pageable pageable);

    @Query("SELECT n FROM InternalNotice n JOIN n.targetDept d WHERE d.id = :targetDeptId ORDER BY n.createdAt DESC")
    Page<Notice> findInternalNoticesByDepartmentOrderByCreatedAt(List<Long> targetDeptId, Pageable pageable);

    @Query("SELECT n FROM InternalNotice n JOIN n.targetDept d WHERE d.id = :departmentId AND n.targetYear = :targetYear ORDER BY n.createdAt DESC")
    Page<Notice> findInternalNoticesByDepartmentAndYearOrderByCreatedAt(Long targetDeptId, TargetYear targetYear,Pageable pageable);


}
