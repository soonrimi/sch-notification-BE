package com.schnofiticationbe.service;

import com.schnofiticationbe.entity.Log;
import com.schnofiticationbe.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Async
    @Transactional
    public void saveLog(Log log) {
        logRepository.save(log);
    }
}