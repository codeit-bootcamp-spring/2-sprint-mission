package com.sprint.mission.discodeit.task;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextCopyingTaskDecorator implements TaskDecorator {

  @Override
  public Runnable decorate(Runnable runnable) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Map<String, String> originalMdc = MDC.getCopyOfContextMap();
    Map<String, String> contextMapCopy = createMdcCopy(originalMdc);

    return () -> {
      Map<String, String> previousMdc = MDC.getCopyOfContextMap();

      try {
        SecurityContextHolder.setContext(securityContext);

        if (contextMapCopy != null && !contextMapCopy.isEmpty()) {
          MDC.setContextMap(contextMapCopy);
          MDC.put("asyncThread", Thread.currentThread().getName());
          MDC.put("asyncStartTime", String.valueOf(System.currentTimeMillis()));
        }

        runnable.run();
      } finally {
        if (previousMdc != null) {
          MDC.setContextMap(previousMdc);
        } else {
          MDC.clear();
        }
        SecurityContextHolder.clearContext();
      }
    };

  }

  private Map<String, String> createMdcCopy(Map<String, String> originalMdc) {
    if (originalMdc == null || originalMdc.isEmpty()) {
      return null;
    }

    Map<String, String> mdcCopy = new HashMap<>();
    originalMdc.forEach((key, value) -> {
      if (key != null && value != null) {
        mdcCopy.put(key, value);
      }
    });

    return mdcCopy;
  }
}
