package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<String> findDistinctTitleBy();

}