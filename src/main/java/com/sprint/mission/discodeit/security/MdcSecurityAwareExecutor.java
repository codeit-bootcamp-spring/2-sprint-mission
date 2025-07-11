package com.sprint.mission.discodeit.security;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MdcSecurityAwareExecutor extends ThreadPoolTaskExecutor {

  @Override
  public void execute(Runnable task) {
    Map<String, String> contextMap = MDC.getCopyOfContextMap();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    super.execute(() -> {
      try {
        if (contextMap != null) {
          MDC.setContextMap(contextMap);
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        task.run();
      } finally {
        MDC.clear();
        SecurityContextHolder.clearContext();
      }
    });
  }
}
