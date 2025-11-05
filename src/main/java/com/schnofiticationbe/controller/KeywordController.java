package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.KeywordDto;
import com.schnofiticationbe.entity.KeywordNotification;
import com.schnofiticationbe.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keywords")
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

    // ★ device 기준 조회 추가 (요청된 변경 사항)
    // 예: GET /keywords/device/{device_id}
    @GetMapping("/device/{device}")
    public ResponseEntity<List<KeywordDto.Response>> getByDevice(@PathVariable("device") String device) {
        List<KeywordDto.Response> list = keywordService.getAllByDevice(device)
                .stream()
                .map(KeywordDto.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KeywordDto.Response> getOne(@PathVariable int id) {
        KeywordNotification k = keywordService.getOne(id);
        return ResponseEntity.ok(new KeywordDto.Response(k));
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

    // 선택: 부분 수정용 엔드포인트가 이미 있다면 그대로 유지 가능
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
