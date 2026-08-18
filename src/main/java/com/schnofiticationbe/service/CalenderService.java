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

    public List<CalenderDto.Response> getAllCalenders(Integer year) {
        if (year != null && (year < 2000 || year > 2100)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Invalid year parameter. Year must be between 2000 and 2100."
            );
        }
        int targetYear = (year != null) ? year : java.time.LocalDate.now(java.time.ZoneId.of("Asia/Seoul")).getYear();
        String yearStart = targetYear + "-01-01";
        String yearEnd = targetYear + "-12-31";

        List<Calender> calenderPage = calenderRepository.findByYear(yearStart, yearEnd);
        return calenderPage.stream()
                .map(this::toEntity)
                .toList();
    }
}
