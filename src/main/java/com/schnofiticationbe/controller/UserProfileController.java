package com.schnofiticationbe.controller;

import com.schnofiticationbe.dto.UserProfileDto;
import com.schnofiticationbe.entity.UserProfile;
import com.schnofiticationbe.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfileDto.Response> create(@RequestBody UserProfileDto.CreateRequest request) {
        UserProfile created = userProfileService.createUserProfile(
                request.getDepartment(),
                request.getGrade(),
                request.getDeviceId()
        );
        return ResponseEntity.ok(new UserProfileDto.Response(created));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto.Response>> getAll() {
        List<UserProfileDto.Response> list = userProfileService.getAll()
                .stream()
                .map(UserProfileDto.Response::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto.Response> getOne(@PathVariable int id) {
        UserProfile p = userProfileService.getOne(id);
        return ResponseEntity.ok(new UserProfileDto.Response(p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto.Response> update(
            @PathVariable int id,
            @RequestBody UserProfileDto.UpdateRequest request) {

        UserProfile updated = userProfileService.update(
                id,
                request.getDepartment(),
                request.getGrade(),
                request.getDeviceId()
        );
        return ResponseEntity.ok(new UserProfileDto.Response(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        userProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
