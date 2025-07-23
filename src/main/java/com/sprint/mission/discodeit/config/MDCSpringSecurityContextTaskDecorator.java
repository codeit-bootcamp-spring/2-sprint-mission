package com.sprint.mission.discodeit.config;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.lang.NonNull; // 이 임포트가 여전히 필요합니다.
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

public class MDCSpringSecurityContextTaskDecorator implements TaskDecorator {
  private final SecurityContextHolderStrategy securityStrategy = SecurityContextHolder.getContextHolderStrategy();

  @Override
  @NonNull
  public Runnable decorate(@NonNull Runnable runnable) {
    Map<String, String> mdcContext = MDC.getCopyOfContextMap();
    SecurityContext securityContext = SecurityContextHolder.getContext();

    return () -> {
      if (mdcContext != null) {
        MDC.setContextMap(mdcContext);
      }

      securityStrategy.setContext(securityContext);

      try {
        runnable.run(); // 실제 비동기 작업 실행
      } finally {
        // 작업 완료 후 컨텍스트 클리어
        MDC.clear();
        securityStrategy.clearContext();
      }
    };
  }
}
