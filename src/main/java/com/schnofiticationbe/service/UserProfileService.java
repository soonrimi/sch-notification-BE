package com.schnofiticationbe.service;

import com.schnofiticationbe.entity.UserProfile;
import com.schnofiticationbe.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfile createUserProfile(String department, Integer grade, String deviceId) {
        UserProfile p = new UserProfile();
        p.setDepartment(department);
        p.setGrade(grade);
        // 엔티티 필드는 device지만, 서비스 파라미터는 deviceId
        p.setDevice(deviceId);
        return userProfileRepository.save(p);
    }

    public List<UserProfile> getAll() {
        return userProfileRepository.findAll();
    }

    public UserProfile getOne(int id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다. id=" + id));
    }

    @Transactional
    public UserProfile update(int id, String department, Integer grade, String deviceId) {
        UserProfile p = userProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 존재하지 않습니다. id=" + id));

        if (department != null && !department.isBlank()) {
            p.setDepartment(department);
        }
        if (grade != null) {
            p.setGrade(grade);
        }
        if (deviceId != null && !deviceId.isBlank()) {
            p.setDevice(deviceId);
        }

        return p;
    }

    public void delete(int id) {
        if (!userProfileRepository.existsById(id)) {
            throw new IllegalArgumentException("사용자 정보가 존재하지 않습니다. id=" + id);
        }
        userProfileRepository.deleteById(id);
    }
}
