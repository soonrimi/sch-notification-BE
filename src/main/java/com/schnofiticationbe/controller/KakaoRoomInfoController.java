package com.schnofiticationbe.controller;

import com.fasterxml.jackson.core.util.ReadConstrainedTextBuffer;
import com.schnofiticationbe.dto.KakaoRoomInfoDto;
import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.service.KakaoRoomInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.DescriptorKey;
import java.util.List;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
public class KakaoRoomInfoController {
    private final KakaoRoomInfoService kakaoRoomInfoService;

    // 생성
    @PostMapping
    public ResponseEntity<KakaoRoomInfoDto.KakaoRoomInfoResponse> createRoom(@RequestBody KakaoRoomInfoDto.CreateKakaoRoomInfoRequest request) {
        KakaoRoomInfoDto.KakaoRoomInfoResponse createdRoom = kakaoRoomInfoService.createKakaoRoomInfo(request);
        return ResponseEntity.ok(createdRoom);
    }

    @Tag(name = "카카오톡방 조회", description = "room_id를 이용해서 카카오톡 방 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<KakaoRoomInfoDto.KakaoRoomInfoResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(kakaoRoomInfoService.findKakaoRoomInfoById(id));
    }

    // 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<KakaoRoomInfoDto.KakaoRoomInfoResponse> updateRoom(@PathVariable Long id, @RequestBody KakaoRoomInfoDto.UpdateKakaoRoomInfoRequest request) {
        KakaoRoomInfoDto.KakaoRoomInfoResponse updatedRoom = kakaoRoomInfoService.updateKakaoRoomInfo(id, request);
        return ResponseEntity.ok(updatedRoom);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        kakaoRoomInfoService.deleteKakaoRoomInfo(id);
        return ResponseEntity.noContent().build();
    }

    @Tag(name = "필터링한 카카오톡 방 정보 조회", description = "학과, 학년 또는 전체를 필터링해 카카오톡 방 정보 조회")
    @GetMapping
    public ResponseEntity<List<KakaoRoomInfoDto.KakaoRoomInfoResponse>> getRoomsByFilter(
            @RequestParam(required = false) Long lessonId,
            @RequestParam(required = false) InternalNotice.TargetYear  targetyear
    ) {
        List<KakaoRoomInfoDto.KakaoRoomInfoResponse> rooms = kakaoRoomInfoService.findRoomsByCriteria(targetyear, lessonId);
        return ResponseEntity.ok(rooms);
    }
}