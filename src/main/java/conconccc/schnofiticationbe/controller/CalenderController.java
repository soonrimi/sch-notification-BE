package conconccc.schnofiticationbe.controller;

import conconccc.schnofiticationbe.dto.CalenderDto;
import conconccc.schnofiticationbe.entity.Calender;
import conconccc.schnofiticationbe.service.CalenderService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calenders")
@RequiredArgsConstructor
@Tag(name = "Calender API", description = "캘린더 CRUD API")
public class CalenderController {

    private final CalenderService CalenderService;

    @GetMapping
    @Operation(summary = "캘린더 전체 조회", description = "모든 캘린더 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "캘린더 조회 성공")
    public ResponseEntity<Page<CalenderDto.Response>> getAllCalenders(Pageable pageable) {
        return ResponseEntity.ok(CalenderService.getAllCalenders(pageable));
    }

    @GetMapping("/{calenderId}")
    @Parameter(name = "calenderId", description = "캘린더 ID", required = true)
    @Operation(summary = "캘린더 상세 조회", description = "캘린더 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "캘린더 조회 성공")
    public ResponseEntity<CalenderDto.Response> getCalender(@PathVariable Long calenderId) {
        return ResponseEntity.ok(CalenderService.getCalender(calenderId));
    }

}
