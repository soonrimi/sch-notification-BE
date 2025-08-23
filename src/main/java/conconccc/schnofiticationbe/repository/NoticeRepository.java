package conconccc.schnofiticationbe.repository;

import conconccc.schnofiticationbe.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByTitleContainingOrContentContaining(String title, String content); // 제목+내용 키워드로 찾기
}
