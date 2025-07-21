package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.common.NotificationEvent;
import com.sprint.mission.discodeit.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {

        String userIdStr = MDC.get("userId");
        if (userIdStr != null) {
            try {
                UUID userId = UUID.fromString(userIdStr);
                NotificationEvent event = new NotificationEvent(
                        userId,
                        NotificationType.ASYNC_FAILED,
                        null,
                        "비동기 작업 실패: " + ex.getMessage()
                );
                eventPublisher.publishEvent(event);
            } catch (Exception e) {
                log.error("알림 이벤트 발행 중 오류 발생", e);
            }
        }
    }
} 