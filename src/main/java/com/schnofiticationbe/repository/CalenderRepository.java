package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Calender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalenderRepository extends JpaRepository<Calender, Long> {

    @Query("SELECT c FROM Calender c WHERE c.startDate <= :yearEnd AND COALESCE(c.endDate, c.startDate) >= :yearStart")
    List<Calender> findByYear(@Param("yearStart") String yearStart, @Param("yearEnd") String yearEnd);
}

