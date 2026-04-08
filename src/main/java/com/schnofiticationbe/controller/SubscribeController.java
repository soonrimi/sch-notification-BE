package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.SubscribeDto;
import com.schnofiticationbe.entity.Subscribe;
import com.schnofiticationbe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping
    public ResponseEntity<SubscribeDto.SubscribeResponse> create(
            @RequestBody SubscribeDto.SubscribeCreateRequest request) {

        Subscribe subscribe = subscribeService.createSubscribe(
                request.getCategory(),
                request.getDeviceId(),
                request.getSubscribed()
        );
        return ResponseEntity.ok(new SubscribeDto.SubscribeResponse(subscribe));
    }

    @GetMapping
    public ResponseEntity<List<SubscribeDto.SubscribeResponse>> getAll() {
        List<SubscribeDto.SubscribeResponse> list = subscribeService.getAllSubscribes()
                .stream()
                .map(SubscribeDto.SubscribeResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // deviceId 기준 조회
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<SubscribeDto.SubscribeResponse>> getByDevice(@PathVariable("deviceId") String deviceId) {
        List<SubscribeDto.SubscribeResponse> list = subscribeService.getSubscribesByDevice(deviceId)
                .stream()
                .map(SubscribeDto.SubscribeResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscribeDto.SubscribeResponse> getOne(@PathVariable int id) {
        Subscribe subscribe = subscribeService.getSubscribe(id);
        return ResponseEntity.ok(new SubscribeDto.SubscribeResponse(subscribe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscribeDto.SubscribeResponse> update(
            @PathVariable int id,
            @RequestBody SubscribeDto.SubscribeUpdateRequest request) {

        Subscribe updated = subscribeService.updateSubscribe(
                id,
                request.getCategory(),
                request.getDeviceId(),
                request.getSubscribed()
        );
        return ResponseEntity.ok(new SubscribeDto.SubscribeResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        subscribeService.deleteSubscribe(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/category")
    public ResponseEntity<SubscribeDto.SubscribeResponse> updateCategory(
            @PathVariable int id,
            @RequestParam String category) {

        Subscribe updated = subscribeService.updateCategory(id, category);
        return ResponseEntity.ok(new SubscribeDto.SubscribeResponse(updated));
    }

    // subscribed만 토글/수정하고 싶을 때 쓰는 API (선택)
    @PatchMapping("/{id}/subscribed")
    public ResponseEntity<SubscribeDto.SubscribeResponse> updateSubscribed(
            @PathVariable int id,
            @RequestParam boolean subscribed) {

        Subscribe updated = subscribeService.updateSubscribed(id, subscribed);
        return ResponseEntity.ok(new SubscribeDto.SubscribeResponse(updated));
    }
}
