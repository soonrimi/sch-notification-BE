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

    public Subscribe createSubscribe(String category, String device) {
        Subscribe subscribe = new Subscribe();
        subscribe.setCategory(category);
        subscribe.setDevice(device);
        return subscribeRepository.save(subscribe);
    }

    public List<Subscribe> getAllSubscribes() {
        return subscribeRepository.findAll();
    }

    public Subscribe getSubscribe(int id) {
        return subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));
    }

    @Transactional
    public Subscribe updateSubscribe(int id, String newCategory, String newDevice) {
        Subscribe subscribe = subscribeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("구독이 존재하지 않습니다. id=" + id));

        if (newCategory != null && !newCategory.isBlank()) {
            subscribe.setCategory(newCategory);
        }
        if (newDevice != null && !newDevice.isBlank()) {
            subscribe.setDevice(newDevice);
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
}
