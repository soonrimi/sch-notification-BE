package com.schnofiticationbe.service;

import com.schnofiticationbe.dto.DeviceDto;
import com.schnofiticationbe.entity.Device;
import com.schnofiticationbe.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    /**
     * 디바이스 등록
     * - 클라이언트에서 받은 deviceToken 을 저장하고
     * - 서버에서 고유 deviceId 를 생성해 반환한다.
     * - 동일한 deviceToken 이 이미 있으면 기존 deviceId 를 재사용한다.
     */
    @Transactional
    public DeviceDto.RegisterResponse register(DeviceDto.RegisterRequest request) {
        return deviceRepository.findByDeviceToken(request.getDeviceToken())
                .map(DeviceDto.RegisterResponse::new)
                .orElseGet(() -> createNewDevice(request));
    }

    private DeviceDto.RegisterResponse createNewDevice(DeviceDto.RegisterRequest request) {
        Device device = new Device();
        device.setDeviceId(UUID.randomUUID().toString());
        device.setDeviceToken(request.getDeviceToken());
        device.setOs(request.getOs());
        device.setAppVersion(request.getAppVersion());

        Device saved = deviceRepository.save(device);
        return new DeviceDto.RegisterResponse(saved);
    }
}

