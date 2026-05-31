package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.CalenderDto;
import com.schnofiticationbe.service.CalenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calenders")
@RequiredArgsConstructor
@Tag(name = "Calender API", description = "캘린더 CRUD API")
public class CalenderController {

    private final CalenderService CalenderService;

    @GetMapping
    @Operation(summary = "캘린더 전체 조회", description = "특정 년도의 캘린더 정보를 조회합니다. 년도를 지정하지 않으면 올해 일정을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "캘린더 조회 성공")
    public ResponseEntity<List<CalenderDto.Response>> getAllCalenders(
            @Parameter(description = "조회할 년도 (기본값: 올해)", example = "2026")
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(CalenderService.getAllCalenders(year));
    }
}
