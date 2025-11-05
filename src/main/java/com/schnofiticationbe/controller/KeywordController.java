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

    // 부분적으로 포함/제외 키워드만 패치하고 싶을 때 (예시)
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
