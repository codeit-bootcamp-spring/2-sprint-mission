package com.sprint.mission.discodeit.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfig {

  @Bean
  MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
    // 모든 매트릭에 태그 부여
    return registry -> registry.config().commonTags("application", "discodeit");
  }

  // @Timed -> TimedAspect Bean 등록 필요
  // @Timed가 붙은 메서드 호출 시
  // Timer 객체를 생성하여 실행 시간 측정
  // MetricRegistry에 해당 정보 등록
  @Bean
  TimedAspect timedAspect(MeterRegistry registry) {
    return new TimedAspect(registry);
  }
}
