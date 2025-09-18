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
        Subscribe subscribe = subscribeService.createSubscribe(request);
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


    @GetMapping("/{id}")
    public ResponseEntity<SubscribeDto.SubscribeResponse> getOne(@PathVariable int id) {
        Subscribe subscribe = subscribeService.getSubscribe(id);
        return ResponseEntity.ok(new SubscribeDto.SubscribeResponse(subscribe));
    }


    @PutMapping("/{id}")
    public ResponseEntity<SubscribeDto.SubscribeResponse> update(
            @PathVariable int id,
            @RequestBody SubscribeDto.SubscribeUpdateRequest request) {

        Subscribe updated = subscribeService.updateSubscribe(id, request);
        return ResponseEntity.ok(new SubscribeDto.SubscribeResponse(updated));
    }

    @PatchMapping("/{id}/keywords")
    public ResponseEntity<SubscribeDto.SubscribeResponse> updateKeywords(
            @PathVariable int id,
            @RequestBody SubscribeDto.SubscribeUpdateKeywordsRequest request) {

        Subscribe updated = subscribeService.updateKeywords(id, request);
        return ResponseEntity.ok(new SubscribeDto.SubscribeResponse(updated));
    }


    @PatchMapping("/{id}/profile")
    public ResponseEntity<SubscribeDto.SubscribeResponse> updateProfile(
            @PathVariable int id,
            @RequestBody SubscribeDto.SubscribeUpdateProfileRequest request) {

        Subscribe updated = subscribeService.updateProfile(id, request);
        return ResponseEntity.ok(new SubscribeDto.SubscribeResponse(updated));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        subscribeService.deleteSubscribe(id);
        return ResponseEntity.noContent().build();
    }
}
