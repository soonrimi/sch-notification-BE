package com.schnofiticationbe.service;

import com.schnofiticationbe.entity.KeywordNotification;
import com.schnofiticationbe.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public KeywordNotification createKeywords(List<String> include, List<String> exclude, String deviceId) {
        KeywordNotification k = new KeywordNotification();
        if (include != null) k.setIncludeKeywords(include);
        if (exclude != null) k.setExcludeKeywords(exclude);
        k.setDevice(deviceId);
        return keywordRepository.save(k);
    }

    public List<KeywordNotification> getAll() {
        return keywordRepository.findAll();
    }

    public List<KeywordNotification> getByDeviceId(String deviceId) {
        return keywordRepository.findByDevice(deviceId);
    }

    @Transactional
    public KeywordNotification update(int id, List<String> include, List<String> exclude, String deviceId) {
        KeywordNotification k = keywordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("키워드 알림이 존재하지 않습니다. id=" + id));

        if (include != null) {
            k.setIncludeKeywords(include);
        }
        if (exclude != null) {
            k.setExcludeKeywords(exclude);
        }
        if (deviceId != null && !deviceId.isBlank()) {
            k.setDevice(deviceId);
        }
        return k;
    }

    public void delete(int id) {
        if (!keywordRepository.existsById(id)) {
            throw new IllegalArgumentException("키워드 알림이 존재하지 않습니다. id=" + id);
        }
        keywordRepository.deleteById(id);
    }
}
