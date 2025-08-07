package com.sprint.mission.discodeit.sse.repository;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public class Emitter {

  private final SseEmitter sseEmitter;
  private final Instant createdAt;


  public Emitter(SseEmitter sseEmitter) {
    this.sseEmitter = sseEmitter;
    this.createdAt = Instant.now();
  }

  public void sendMessage(String id, String name, Object data) throws IOException {
    sseEmitter.send(SseEmitter.event()
        .id(id)
        .name(name)
        .data(data)
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Emitter emitter = (Emitter) o;

    return Objects.equals(sseEmitter, emitter.sseEmitter);
  }

  @Override
  public int hashCode() {
    return sseEmitter != null ? sseEmitter.hashCode() : 0;
  }

}
