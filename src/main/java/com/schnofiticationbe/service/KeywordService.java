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

    public KeywordNotification createKeywords(List<String> includeKeywords, List<String> excludeKeywords, String device) {
        KeywordNotification k = new KeywordNotification();
        if (includeKeywords != null) k.setIncludeKeywords(includeKeywords);
        if (excludeKeywords != null) k.setExcludeKeywords(excludeKeywords);
        k.setDevice(device);
        return keywordRepository.save(k);
    }

    public List<KeywordNotification> getAll() {
        return keywordRepository.findAll();
    }

    public List<KeywordNotification> getAllByDevice(String device) {
        return keywordRepository.findByDevice(device);
    }

    public KeywordNotification getOne(int id) {
        return keywordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("키워드 알림이 존재하지 않습니다. id=" + id));
    }

    @Transactional
    public KeywordNotification update(int id, List<String> includeKeywords, List<String> excludeKeywords, String device) {
        KeywordNotification k = keywordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("키워드 알림이 존재하지 않습니다. id=" + id));

        if (includeKeywords != null) {
            k.setIncludeKeywords(includeKeywords);
        }
        if (excludeKeywords != null) {
            k.setExcludeKeywords(excludeKeywords);
        }
        if (device != null && !device.isBlank()) {
            k.setDevice(device);
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
