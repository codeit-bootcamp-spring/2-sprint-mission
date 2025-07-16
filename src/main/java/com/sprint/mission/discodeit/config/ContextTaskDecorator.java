package com.sprint.mission.discodeit.config;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ContextTaskDecorator implements TaskDecorator {

  @Override
  public Runnable decorate(Runnable runnable) {
    // 현재 요청을 처리 중인 스레드에서 실행 (decorate를 호출하는 쓰레드)
    // 현재 스레드의 컨텍스트 정보를 캡처
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Map<String, String> mdcContext = MDC.getCopyOfContextMap();

    return () -> {
      try {
        // 비동기 스레드에 컨텍스트 정보 설정 (복사)
        SecurityContextHolder.setContext(securityContext);
        if (mdcContext != null) {
          MDC.setContextMap(mdcContext);
        }
        runnable.run();
      } finally {
        // 새로운 비동기 스레드의 컨텍스트 정리
        SecurityContextHolder.clearContext();
        MDC.clear();
      }
    };
  }
}
