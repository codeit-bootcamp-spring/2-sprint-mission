package com.sprint.mission.discodeit.config;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextTaskDecorator implements TaskDecorator {

  @Override
  public Runnable decorate(Runnable runnable) {
    Map<String, String> contextMap = MDC.getCopyOfContextMap();
    SecurityContext securityContext = SecurityContextHolder.getContext();

    return () -> {
      try {
        // MDC 설정
        if (contextMap != null) {
          MDC.setContextMap(contextMap);
        }
        // SecurityContext 설정
        SecurityContextHolder.setContext(securityContext);
        // 비동기 작업 실행
        runnable.run();
      } finally {
        // MDC 정리
        MDC.clear();
        SecurityContextHolder.clearContext();
      }
    };
  }
}
