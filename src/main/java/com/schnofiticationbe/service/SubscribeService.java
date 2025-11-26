package com.schnofiticationbe.service;

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

    public Subscribe createSubscribe(String category, String deviceId, Boolean subscribed) {
        Subscribe subscribe = new Subscribe();
        subscribe.setCategory(category);
        subscribe.setDevice(deviceId);
        // null이면 기본값 true
        subscribe.setSubscribed(subscribed == null ? true : subscribed);
        return subscribeRepository.save(subscribe);
    }

    public List<Subscribe> getAllSubscribes() {
        return subscribeRepository.findAll();
    }

    public List<Subscribe> getSubscribesByDevice(String deviceId) {
        return subscribeRepository.findByDevice(deviceId);
    }

    public Subscribe getSubscribe(int id) {
        return subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));
    }

    @Transactional
    public Subscribe updateSubscribe(int id, String newCategory, String newDeviceId, Boolean newSubscribed) {
        Subscribe subscribe = subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));

        if (newCategory != null && !newCategory.isBlank()) {
            subscribe.setCategory(newCategory);
        }
        if (newDeviceId != null && !newDeviceId.isBlank()) {
            subscribe.setDevice(newDeviceId);
        }
        if (newSubscribed != null) {
            subscribe.setSubscribed(newSubscribed);
        }

        return subscribe;
    }

    public void deleteSubscribe(int id) {
        if (!subscribeRepository.existsById(id)) {
            throw new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id);
        }
        subscribeRepository.deleteById(id);
    }

    @Transactional
    public Subscribe updateCategory(int id, String newCategory) {
        Subscribe subscribe = subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));
        subscribe.setCategory(newCategory);
        return subscribe;
    }

    // ✅ subscribed만 따로 수정하는 메서드 (원하면 사용)
    @Transactional
    public Subscribe updateSubscribed(int id, boolean subscribed) {
        Subscribe s = subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));
        s.setSubscribed(subscribed);
        return s;
    }
}
