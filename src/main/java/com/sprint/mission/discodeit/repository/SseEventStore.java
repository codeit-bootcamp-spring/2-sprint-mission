package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.event.sse.SseStoredEvent;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEventStore {

  private static final String KEY_PREFIX = "sse:events:";
  private static final Duration EVENT_TTL = Duration.ofMinutes(5);

  private final RedisTemplate<String, Object> redisTemplate;

  public void saveEvent(UUID userId, SseStoredEvent event) {
    String key = KEY_PREFIX + userId.toString();

    try {
      ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
      zSetOps.add(key, event, event.timestamp().toEpochMilli());

      redisTemplate.expire(key, EVENT_TTL);

      long cutoffTime = System.currentTimeMillis() - EVENT_TTL.toMillis();
      zSetOps.removeRangeByScore(key, 0, cutoffTime);

      log.debug("SSE 이벤트 저장: userId={}, eventId={}, eventType={}",
          userId, event.eventId(), event.eventType());
    } catch (Exception e) {
      log.error("SSE 이벤트 저장 실패: userId={}, eventId={}", userId, event.eventId(), e);
    }
  }


  public List<SseStoredEvent> getEventsSince(UUID userId, String lastEventId) {
    String key = KEY_PREFIX + userId.toString();

    try {
      long lastTimestamp = parseEventIdToTimestamp(lastEventId);

      Set<Object> events = redisTemplate.opsForZSet()
          .rangeByScore(key, lastTimestamp + 1, Double.MAX_VALUE);

      if (events == null) {
        return List.of();
      }

      List<SseStoredEvent> result = events.stream()
          .filter(obj -> obj instanceof SseStoredEvent)
          .map(obj -> (SseStoredEvent) obj)
          .collect(Collectors.toList());

      log.debug("SSE 이벤트 조회: userId={}, lastEventId={}, count={}",
          userId, lastEventId, result.size());

      return result;
    } catch (Exception e) {
      log.error("SSE 이벤트 조회 실패: userId={}, lastEventId={}", userId, lastEventId, e);
      return List.of();
    }
  }

  public void clearUserEvents(UUID userId) {
    String key = KEY_PREFIX + userId.toString();
    try {
      redisTemplate.delete(key);
      log.debug("SSE 이벤트 삭제: userId={}", userId);
    } catch (Exception e) {
      log.error("SSE 이벤트 삭제 실패: userId={}", userId, e);
    }
  }


  private long parseEventIdToTimestamp(String eventId) {
    if (eventId == null || eventId.isEmpty()) {
      return 0;
    }

    try {
      String[] parts = eventId.split("-");
      if (parts.length >= 1) {
        return Long.parseLong(parts[0]);
      }
    } catch (NumberFormatException e) {
      log.warn("잘못된 이벤트 ID 형식: {}", eventId);
    }

    return 0;
  }
}