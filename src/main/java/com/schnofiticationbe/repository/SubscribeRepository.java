package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

    // device 기준 조회
    List<Subscribe> findByDevice(String device);

    // (선택) ID와 device를 함께 확인하고 싶을 때 사용할 수 있음
    boolean existsByIdAndDevice(Integer id, String device);
}
