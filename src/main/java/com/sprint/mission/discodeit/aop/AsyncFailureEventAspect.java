package com.sprint.mission.discodeit.aop;

import com.sprint.mission.discodeit.annotation.AsyncTaskLabel;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.AsyncFailureNotificationEvent;
import java.lang.reflect.Method;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AsyncFailureEventAspect {

  private final ApplicationEventPublisher eventPublisher;

  @Pointcut("@annotation(org.springframework.scheduling.annotation.Async)")
  public void asyncMethods() {}

  @Around("asyncMethods()")
  public Object aroundAsync(ProceedingJoinPoint joinPoint){
    try {
      return joinPoint.proceed();
    } catch (Throwable ex) {
      Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
      String label = Optional.ofNullable(method.getAnnotation(AsyncTaskLabel.class))
              .map(AsyncTaskLabel::value)
                  .orElse("알 수 없는 작업");
      eventPublisher.publishEvent(new AsyncFailureNotificationEvent(label + " 실패", label + " 실패", NotificationType.ASYNC_FAILED));
      throw new RuntimeException(ex);
    }
  }
}
