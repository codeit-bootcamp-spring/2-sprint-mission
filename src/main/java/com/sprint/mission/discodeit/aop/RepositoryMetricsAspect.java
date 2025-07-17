package com.sprint.mission.discodeit.aop;

import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryMetricsAspect {

  private final MeterRegistry meterRegistry;

  public RepositoryMetricsAspect(MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
  }

  @Pointcut("execution(* com.sprint.mission.discodeit.core..repository.*.*(..))")
  public void repositoryMethods() {
  }

  @Before("repositoryMethods()")
  public void countRepositoryCalls(JoinPoint joinPoint) {
    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String methodName = joinPoint.getSignature().getName();

    // 'jpa.execution.count' 라는 이름의 카운터를 1 증가시킵니다.
    meterRegistry.counter("jpa.execution.count", "class", className, "method", methodName)
        .increment();
  }
}