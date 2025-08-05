package com.sprint.mission.discodeit.sse;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class SseEmitterWrapper {

  private final SseEmitter emitter;
  private final UUID emitterId;
  private volatile Instant lastActiveAt;

  public SseEmitterWrapper(SseEmitter emitter) {
    this.emitter = emitter;
    this.emitterId = UUID.randomUUID();
    this.lastActiveAt = Instant.now();
  }

  public void updateLastActiveAt() {
    this.lastActiveAt = Instant.now();
  }
}
