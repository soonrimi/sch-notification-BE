package com.schnofiticationbe.config; // 또는 적절한 패키지

import com.schnofiticationbe.entity.Log;
import org.springframework.stereotype.Component;

@Component
public class LogContextHolder {

    private final ThreadLocal<Log.LogBuilder> logBuilderThreadLocal = new ThreadLocal<>();

    /**
     * 현재 스레드의 로그 기록지를 초기화합니다.
     */
    public void init(Log.LogBuilder logBuilder) {
        logBuilderThreadLocal.set(logBuilder);
    }

    /**
     * 현재 스레드의 로그 기록지를 가져옵니다.
     */
    public Log.LogBuilder get() {
        return logBuilderThreadLocal.get();
    }

    /**
     * 현재 스레드의 로그 기록지를 삭제하여 메모리 누수를 방지합니다.
     */
    public void clear() {
        logBuilderThreadLocal.remove();
    }
}