package com.schnofiticationbe.repository;

import com.schnofiticationbe.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String> {

    Optional<Device> findByDeviceToken(String deviceToken);
}

