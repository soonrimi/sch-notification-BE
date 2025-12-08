package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

    // device 기준 조회
    List<Subscribe> findByDevice(String device);

    // ID와 device를 함께 확인하고 싶을 때 사용할 수 있음
    boolean existsByIdAndDevice(Integer id, String device);

    // 카테고리 기준 + 구독 상태가 true 인 것만 조회
    List<Subscribe> findByCategoryAndSubscribedTrue(String category);
}
