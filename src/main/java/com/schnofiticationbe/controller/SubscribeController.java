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
@RequestMapping("/api/subscribe")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping
    public ResponseEntity<SubscribeDto.Response> create(@RequestBody SubscribeDto.CreateRequest request) {
        Subscribe subscribe = subscribeService.createSubscribe(request.getCategory(), request.getDevice());
        return ResponseEntity.ok(new SubscribeDto.Response(subscribe));
    }

    @GetMapping
    public ResponseEntity<List<SubscribeDto.Response>> getAll() {
        List<SubscribeDto.Response> list = subscribeService.getAllSubscribes()
                .stream()
                .map(SubscribeDto.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscribeDto.Response> getOne(@PathVariable int id) {
        Subscribe subscribe = subscribeService.getSubscribe(id);
        return ResponseEntity.ok(new SubscribeDto.Response(subscribe));
    }


    @PutMapping("/{id}")
    public ResponseEntity<SubscribeDto.Response> update(
            @PathVariable int id,
            @RequestBody SubscribeDto.UpdateRequest request) {

        Subscribe updated = subscribeService.updateSubscribe(id, request.getCategory(), request.getDevice());
        return ResponseEntity.ok(new SubscribeDto.Response(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        subscribeService.deleteSubscribe(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/category")
    public ResponseEntity<SubscribeDto.Response> updateCategory(
            @PathVariable int id,
            @RequestParam String category) {

        Subscribe updated = subscribeService.updateCategory(id, category);
        return ResponseEntity.ok(new SubscribeDto.Response(updated));
    }
}
