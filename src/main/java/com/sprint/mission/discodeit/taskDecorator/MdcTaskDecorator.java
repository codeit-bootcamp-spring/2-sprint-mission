package com.sprint.mission.discodeit.taskDecorator;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.HashMap;
import java.util.Map;

public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 현재 스레드의 MDC 컨텍스트 맵을 깊은 복사로 캡처
        Map<String, String> originalContextMap = MDC.getCopyOfContextMap();
        Map<String, String> contextMapCopy = createMdcCopy(originalContextMap);

        return () -> {
            // 비동기 스레드의 기존 MDC 백업
            Map<String, String> previousMdcContext = MDC.getCopyOfContextMap();

            try {
                // 캡처된 MDC 컨텍스트 설정
                if (contextMapCopy != null && !contextMapCopy.isEmpty()) {
                    MDC.setContextMap(contextMapCopy);

                    // 추가 컨텍스트 정보 설정 (스레드 이름, 실행 시작 시간 등)
                    MDC.put("asyncThread", Thread.currentThread().getName());
                    MDC.put("asyncStartTime", String.valueOf(System.currentTimeMillis()));
                }

                runnable.run();

            } finally {
                // MDC 복원 또는 정리
                if (previousMdcContext != null) {
                    MDC.setContextMap(previousMdcContext);
                } else {
                    MDC.clear();
                }
            }
        };
    }

    /**
     * MDC 컨텍스트의 깊은 복사를 수행한다.
     * null 체크와 불변성 보장을 위해 새로운 HashMap으로 복사한다.
     */
    private Map<String, String> createMdcCopy(Map<String, String> original) {
        if (original == null || original.isEmpty()) {
            return null;
        }

        // 깊은 복사로 새로운 HashMap 생성
        Map<String, String> copy = new HashMap<>();
        original.forEach((key, value) -> {
            if (key != null && value != null) {
                copy.put(key, value);
            }
        });

        return copy;
    }

}
