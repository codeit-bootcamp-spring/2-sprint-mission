package com.sprint.mission.discodeit.listener.sse;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class SseEventIdGenerator {

  private final AtomicLong sequence = new AtomicLong(0);

  public String generateEventId() {
    long timestamp = System.currentTimeMillis();
    long seq = sequence.incrementAndGet() % 1000;
    return String.format("%d-%03d", timestamp, seq);
  }
}