package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.KakaoRoomInfoDto;
import com.schnofiticationbe.entity.InternalNotice;
import com.schnofiticationbe.entity.KakaoRoomInfo;
import com.schnofiticationbe.repository.KakaoRoomInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KakaoRoomInfoService {
    private final KakaoRoomInfoRepository kakaoRoomInfoRepository;

    // CREATE KakaoRoom
    @Transactional
    public KakaoRoomInfoDto.KakaoRoomInfoResponse createKakaoRoomInfo(KakaoRoomInfoDto.CreateKakaoRoomInfoRequest request) {
        KakaoRoomInfo newKakaoRoomInfo = KakaoRoomInfo.from(request);
        KakaoRoomInfo savedInfo=kakaoRoomInfoRepository.save(newKakaoRoomInfo);
        return new KakaoRoomInfoDto.KakaoRoomInfoResponse(savedInfo);
    }

    // READ by ID
    @Transactional(readOnly = true)
    public KakaoRoomInfoDto.KakaoRoomInfoResponse findKakaoRoomInfoById(Long id) {
        KakaoRoomInfo kakaoRoomInfo = kakaoRoomInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KakaoRoomInfo not found with id: " + id));
        return new KakaoRoomInfoDto.KakaoRoomInfoResponse(kakaoRoomInfo);
    }

    // UPDATE
    @Transactional
    public KakaoRoomInfoDto.KakaoRoomInfoResponse updateKakaoRoomInfo(Long id, KakaoRoomInfoDto.UpdateKakaoRoomInfoRequest request) {
        KakaoRoomInfo existingKakaoRoomInfo = kakaoRoomInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("KakaoRoomInfo not found with id: " + id));

        existingKakaoRoomInfo.setLessonId(request.getLessonId());
        existingKakaoRoomInfo.setTargetYear(InternalNotice.TargetYear.valueOf(request.getTargetYear()));

        KakaoRoomInfo updatedInfo = kakaoRoomInfoRepository.save(existingKakaoRoomInfo);
        return new KakaoRoomInfoDto.KakaoRoomInfoResponse(updatedInfo);
    }

    // DELETE
    @Transactional
    public void deleteKakaoRoomInfo(Long id) {
        if (!kakaoRoomInfoRepository.existsById(id)) {
            throw new EntityNotFoundException("KakaoRoomInfo not found with id: " + id);
        }
        kakaoRoomInfoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<KakaoRoomInfoDto.KakaoRoomInfoResponse> findRoomsByCriteria(InternalNotice.TargetYear targetYear, Long lessonId) {
        List<KakaoRoomInfo> results;

        if (lessonId != null && targetYear != null) {
            // 1. lessonId와 targetYear가 모두 있는 경우
            results = kakaoRoomInfoRepository.findByLessonIdAndTargetYear(lessonId, targetYear);
        } else if (lessonId != null) {
            // 2. lessonId만 있는 경우
            results = kakaoRoomInfoRepository.findAllByLessonId(lessonId);
        } else {
            // 3. 파라미터가 모두 없는 경우 (getAll)
            results = kakaoRoomInfoRepository.findAll();
        }

        // 조회된 Entity 리스트를 DTO 리스트로 변환하여 반환
        return results.stream()
                .map(KakaoRoomInfoDto.KakaoRoomInfoResponse::new)
                .collect(Collectors.toList());
    }
}