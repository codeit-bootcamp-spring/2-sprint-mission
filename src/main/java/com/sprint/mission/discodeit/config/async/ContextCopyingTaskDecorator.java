package com.sprint.mission.discodeit.config.async;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ContextCopyingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 현재 스레드의 MDC, SecurityContext 복사
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        SecurityContext securityContext = SecurityContextHolder.getContext();

        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            SecurityContext previousSercurityContext = SecurityContextHolder.getContext();
            try {
                if (contextMap != null) MDC.setContextMap(contextMap);
                else MDC.clear();
                SecurityContextHolder.setContext(securityContext);
                runnable.run();
            } finally {
                // cleanup
                if (previous != null) MDC.setContextMap(previous);
                else MDC.clear();
                SecurityContextHolder.setContext(previousSercurityContext);
            }
        };
    }
}