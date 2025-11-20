package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.KeywordDto;
import com.schnofiticationbe.entity.KeywordNotification;
import com.schnofiticationbe.service.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keyword")
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping
    public ResponseEntity<KeywordDto.Response> create(@RequestBody KeywordDto.CreateRequest request) {
        KeywordNotification created = keywordService.createKeywords(
                request.getIncludeKeywords(),
                request.getExcludeKeywords(),
                request.getDevice()
        );
        return ResponseEntity.ok(new KeywordDto.Response(created));
    }

    @GetMapping
    public ResponseEntity<List<KeywordDto.Response>> getAll() {
        List<KeywordDto.Response> list = keywordService.getAll()
                .stream()
                .map(KeywordDto.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/device/{device_id}")
    @Operation(summary = "디바이스별 키워드 조회", description = "device_id를 기준으로 키워드 알림 목록을 조회합니다.")
    public ResponseEntity<List<KeywordDto.Response>> getByDeviceId(
            @Parameter(description = "디바이스 ID", required = true) @PathVariable("device_id") String deviceId) {
        List<KeywordDto.Response> list = keywordService.getByDeviceId(deviceId)
                .stream()
                .map(KeywordDto.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KeywordDto.Response> update(
            @PathVariable int id,
            @RequestBody KeywordDto.UpdateRequest request) {

        KeywordNotification updated = keywordService.update(
                id,
                request.getIncludeKeywords(),
                request.getExcludeKeywords(),
                request.getDevice()
        );
        return ResponseEntity.ok(new KeywordDto.Response(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        keywordService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/include")
    public ResponseEntity<KeywordDto.Response> patchInclude(
            @PathVariable int id,
            @RequestBody List<String> includeKeywords) {

        KeywordNotification k = keywordService.update(id, includeKeywords, null, null);
        return ResponseEntity.ok(new KeywordDto.Response(k));
    }

    @PatchMapping("/{id}/exclude")
    public ResponseEntity<KeywordDto.Response> patchExclude(
            @PathVariable int id,
            @RequestBody List<String> excludeKeywords) {

        KeywordNotification k = keywordService.update(id, null, excludeKeywords, null);
        return ResponseEntity.ok(new KeywordDto.Response(k));
    }
}
