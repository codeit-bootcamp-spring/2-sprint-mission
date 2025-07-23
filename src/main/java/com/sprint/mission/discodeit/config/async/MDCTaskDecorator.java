package com.sprint.mission.discodeit.config.async;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class MDCTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 현재 스레드의 MDC
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            try {
                if (contextMap != null) MDC.setContextMap(contextMap);
                else MDC.clear();
                runnable.run();
            } finally {
                // cleanup
                if (previous != null) MDC.setContextMap(previous);
                else MDC.clear();
            }
        };
    }
}