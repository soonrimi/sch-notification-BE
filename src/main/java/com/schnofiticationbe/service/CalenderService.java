package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.CalenderDto;
import com.schnofiticationbe.entity.Calender;
import com.schnofiticationbe.repository.CalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final CalenderRepository calenderRepository;

    public CalenderDto.Response createCalender(CalenderDto.CreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("캘린더 정보가 필요합니다.");
        }
        Calender calender = Calender.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .content(request.getContent())
                .build();

        Calender saved= calenderRepository.save(calender);
        return toEntity(saved);
    }

    public CalenderDto.Response getCalender(Long calenderId) {
        return calenderRepository.findById(calenderId)
                .map(this::toEntity)
                .orElseThrow(() -> new IllegalArgumentException("캘린더가 존재하지 않습니다."));
    }

    public CalenderDto.Response updateRequest(Long CalenderId, CalenderDto.UpdateRequest request) {
        if (calenderRepository.existsById(CalenderId)) {
            Calender calender = calenderRepository.findById(CalenderId)
                    .orElseThrow(() -> new IllegalArgumentException("캘린더가 존재하지 않습니다."));

            if (request.getTitle() != null ) {
                calender.setTitle(request.getTitle());}
            else if (request.getStartDate() != null) {
                calender.setStartDate(request.getStartDate());
            } else if (request.getEndDate() != null) {
                calender.setEndDate(request.getEndDate());
            } else if (request.getContent() != null) {
                calender.setContent(request.getContent());
            } else {
                throw new IllegalArgumentException("업데이트할 정보가 없습니다.");
            }

            Calender updated = calenderRepository.save(calender);
            return toEntity(updated);
        } else {
            throw new IllegalArgumentException("캘린더가 존재하지 않습니다.");
        }
    }

    public void deleteCalender(Long calenderId) {
        if (calenderRepository.existsById(calenderId)) {
            calenderRepository.deleteById(calenderId);
        } else {
            throw new IllegalArgumentException("캘린더가 존재하지 않습니다.");
        }
    }

    public CalenderDto.Response toEntity(Calender calender) {
        if (calender == null) {
            throw new IllegalArgumentException("캘린더 정보가 필요합니다.");
        }
        return CalenderDto.Response.builder()
                .id(calender.getId())
                .title(calender.getTitle())
                .startDate(calender.getStartDate())
                .endDate(calender.getEndDate())
                .content(calender.getContent())
                .build();
    }

    public Page<CalenderDto.Response> getAllCalenders(Pageable pageable) {
        Page<Calender> calenderPage = calenderRepository.findAll(pageable);
        return calenderPage.map(this::toEntity);
    }
}
