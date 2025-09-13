package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.KakaoRoomInfoDto;
import com.schnofiticationbe.entity.Department;
import com.schnofiticationbe.entity.KakaoRoomInfo;
import com.schnofiticationbe.entity.TargetYear;
import com.schnofiticationbe.repository.DepartmentRepository;
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
    private final DepartmentRepository departmentRepository;

    // CREATE KakaoRoom
    @Transactional
    public KakaoRoomInfoDto.KakaoRoomInfoResponse createKakaoRoomInfo(KakaoRoomInfoDto.CreateKakaoRoomInfoRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + request.getDepartmentId()));
        KakaoRoomInfo newKakaoRoomInfo = KakaoRoomInfo.from(department, request);
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

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + request.getDepartmentId()));
        existingKakaoRoomInfo.setDepartment(department);
        existingKakaoRoomInfo.setTargetYear(request.getTargetYear());
        existingKakaoRoomInfo.setRoomName(request.getRoomName());

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
    public List<KakaoRoomInfoDto.KakaoRoomInfoResponse> findRoomsByCriteria(TargetYear targetYear, Long lessonId) {
        List<KakaoRoomInfo> results;

        // Since lessonId is not a property, we can only filter by targetYear or return all
        if (targetYear != null) {
            // Filter in-memory since no repository method exists
            results = kakaoRoomInfoRepository.findAll().stream()
                .filter(room -> room.getTargetYear() == targetYear)
                .collect(Collectors.toList());
        } else {
            results = kakaoRoomInfoRepository.findAll();
        }

        return results.stream()
                .map(KakaoRoomInfoDto.KakaoRoomInfoResponse::new)
                .collect(Collectors.toList());
    }
}