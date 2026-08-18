package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Category;
import com.schnofiticationbe.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long>, JpaSpecificationExecutor<Notice> {
    
    // [성능 개선] 복합 타겟 조회 성능 확보를 위한 ID+Date 목록 추출 네이티브 쿼리
    @Query(value = "SELECT DISTINCT n.id, n.created_at FROM notice n " +
           "JOIN internal_notice in_n ON n.id = in_n.id " +
           "JOIN internal_notice_target_dept intd ON in_n.id = intd.internal_notice_id " +
           "WHERE intd.department_id = :deptId AND (in_n.target_year = :targetYear OR in_n.target_year = 0)", nativeQuery = true)
    List<Object[]> findInternalNoticeIdsAndDateByBundle(@Param("deptId") Long deptId, @Param("targetYear") int targetYear);

    // [성능 개선] 크롤링 소스명에 해당하는 CrawlPosts ID+Date 목록 조회 네이티브 쿼리
    @Query(value = "SELECT cp.id, n.created_at FROM crawl_posts cp " +
           "JOIN notice n ON cp.id = n.id " +
           "WHERE cp.source IN :deptNames", nativeQuery = true)
    List<Object[]> findCrawlPostIdsAndDateBySources(@Param("deptNames") List<String> deptNames);

    // [성능 개선] 일반 공지사항 ID+Date 목록 조회 네이티브 쿼리
    @Query(value = "SELECT n.id, n.created_at FROM notice n WHERE n.category IN :categories", nativeQuery = true)
    List<Object[]> findGeneralNoticeIdsAndDate(@Param("categories") List<String> categories);

    // [성능 개선] 학과/학년별 최적화된 ID+Date 추출 네이티브 쿼리
    @Query(value = "SELECT DISTINCT n.id, n.created_at FROM notice n " +
           "JOIN internal_notice in_n ON n.id = in_n.id " +
           "JOIN internal_notice_target_dept intd ON in_n.id = intd.internal_notice_id " +
           "WHERE intd.department_id = :deptId AND in_n.target_year = :targetYear", nativeQuery = true)
    List<Object[]> findInternalNoticeIdsAndDateByExactBundle(@Param("deptId") Long deptId, @Param("targetYear") int targetYear);

    // [성능 개선] JPQL을 사용하여 상속 매핑 엔티티를 안전하고 신속하게 로드 (ID 필터링으로 조인 최소화, 10ms 이하)
    @Query("SELECT n FROM Notice n WHERE n.id IN :matchedIds ORDER BY n.createdAt DESC")
    Page<Notice> findNoticesByMatchedIdsOnly(
            @Param("matchedIds") List<Long> matchedIds,
            Pageable pageable);

    // [성능 개선] 키워드 기반 ID 페이징 네이티브 쿼리 (상속 조인 방지)
    @Query(value = "SELECT n.id FROM notice n WHERE n.title LIKE CONCAT('%', :keyword, '%') OR n.content LIKE CONCAT('%', :keyword, '%') ORDER BY n.created_at DESC", nativeQuery = true)
    Page<Long> findIdsByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);

    // [성능 개선] 카테고리 기반 ID 페이징 네이티브 쿼리 (상속 조인 방지)
    @Query(value = "SELECT n.id FROM notice n WHERE n.category = :category ORDER BY n.created_at DESC", nativeQuery = true)
    Page<Long> findIdsByCategory(@Param("category") String category, Pageable pageable);

    // [성능 개선] 전체 ID 페이징 네이티브 쿼리 (상속 조인 방지)
    @Query(value = "SELECT n.id FROM notice n ORDER BY n.created_at DESC", nativeQuery = true)
    Page<Long> findAllIds(Pageable pageable);

    // [성능 개선] 북마크 목록 ID 페이징 네이티브 쿼리 (상속 조인 방지)
    @Query(value = "SELECT n.id FROM notice n WHERE n.id IN :ids ORDER BY n.created_at DESC", nativeQuery = true)
    Page<Long> findIdsByIdIn(@Param("ids") List<Long> ids, Pageable pageable);

    // [성능 개선] 북마크 내 검색 ID 페이징 네이티브 쿼리 (상속 조인 방지)
    @Query(value = "SELECT n.id FROM notice n WHERE (n.id IN :ids) AND (n.title LIKE CONCAT('%', :keyword, '%') OR n.content LIKE CONCAT('%', :keyword, '%')) ORDER BY n.created_at DESC", nativeQuery = true)
    Page<Long> findIdsByIdInAndKeyword(@Param("ids") List<Long> ids, @Param("keyword") String keyword, Pageable pageable);

    // [성능 개선] 학과별 공지 ID 페이징 네이티브 쿼리 (상속 조인 방지)
    @Query(value = "SELECT DISTINCT n.id FROM notice n " +
           "JOIN internal_notice in_n ON n.id = in_n.id " +
           "JOIN internal_notice_target_dept intd ON in_n.id = intd.internal_notice_id " +
           "WHERE intd.department_id IN :targetDeptIds ORDER BY n.created_at DESC", nativeQuery = true)
    Page<Long> findInternalNoticeIdsByDepartment(@Param("targetDeptIds") List<Long> targetDeptIds, Pageable pageable);
}
