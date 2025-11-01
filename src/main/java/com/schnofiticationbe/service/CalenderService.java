package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.CalenderDto;
import com.schnofiticationbe.entity.Calender;
import com.schnofiticationbe.repository.CalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final CalenderRepository calenderRepository;

    public CalenderDto.Response toEntity(Calender calender) {
        if (calender == null) {
            throw new IllegalArgumentException("캘린더 정보가 필요합니다.");
        }
        return CalenderDto.Response.builder()
                .id(calender.getId())
                .title(calender.getTitle())
                .startDate(calender.getStartDate())
                .endDate(calender.getEndDate())
                .type(calender.getType())
                .build();
    }

    public List<CalenderDto.Response> getAllCalenders() {
        List<Calender> calenderPage = calenderRepository.findAll();
        return calenderPage.stream()
                .map(this::toEntity)
                .toList();
    }
}
