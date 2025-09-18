package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.SubscribeDto;
import com.schnofiticationbe.entity.Subscribe;
import com.schnofiticationbe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;


    public Subscribe createSubscribe(SubscribeDto.SubscribeCreateRequest req) {
        if (req.getDevice() == null || req.getDevice().isBlank()) {
            throw new IllegalArgumentException("device는 필수입니다.");
        }

        Subscribe s = new Subscribe();
        s.setDevice(req.getDevice());
        s.setCategory(req.getCategory());
        s.setDepartment(req.getDepartment());
        s.setYear(req.getYear());

        if (req.getIncludeKeywords() != null) s.setIncludeKeywords(req.getIncludeKeywords());
        if (req.getExcludeKeywords() != null) s.setExcludeKeywords(req.getExcludeKeywords());

        return subscribeRepository.save(s);
    }

    public List<Subscribe> getAllSubscribes() {
        return subscribeRepository.findAll();
    }

    public Subscribe getSubscribe(int id) {
        return subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));
    }


    @Transactional
    public Subscribe updateSubscribe(int id, SubscribeDto.SubscribeUpdateRequest req) {
        Subscribe s = subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));

        if (req.getDevice() != null && !req.getDevice().isBlank()) s.setDevice(req.getDevice());
        if (req.getCategory() != null) s.setCategory(req.getCategory());
        if (req.getDepartment() != null) s.setDepartment(req.getDepartment());
        if (req.getYear() != null) s.setYear(req.getYear());


        if (req.getIncludeKeywords() != null) s.setIncludeKeywords(req.getIncludeKeywords());
        if (req.getExcludeKeywords() != null) s.setExcludeKeywords(req.getExcludeKeywords());

        return s;
    }


    @Transactional
    public Subscribe updateKeywords(int id, SubscribeDto.SubscribeUpdateKeywordsRequest req) {
        Subscribe s = subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));

        if (req.getIncludeKeywords() != null) s.setIncludeKeywords(req.getIncludeKeywords());
        if (req.getExcludeKeywords() != null) s.setExcludeKeywords(req.getExcludeKeywords());

        return s;
    }


    @Transactional
    public Subscribe updateProfile(int id, SubscribeDto.SubscribeUpdateProfileRequest req) {
        Subscribe s = subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));

        if (req.getDepartment() != null) s.setDepartment(req.getDepartment());
        if (req.getYear() != null) s.setYear(req.getYear());
        if (req.getDevice() != null && !req.getDevice().isBlank()) s.setDevice(req.getDevice());

        return s;
    }

    public void deleteSubscribe(int id) {
        if (!subscribeRepository.existsById(id)) {
            throw new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id);
        }
        subscribeRepository.deleteById(id);
    }
}
