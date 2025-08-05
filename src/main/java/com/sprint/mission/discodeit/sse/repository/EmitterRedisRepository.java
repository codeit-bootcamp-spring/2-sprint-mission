package com.sprint.mission.discodeit.sse.repository;

import com.sprint.mission.discodeit.sse.SseEmitterWrapper;
import com.sprint.mission.discodeit.sse.SseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class EmitterRedisRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  private String emitterKey(UUID userId) {
    return "sse:emitters:" + userId;
  }

  private String eventKey(UUID userId) {
    return "sse:events:" + userId;
  }


  public void save(UUID userId, String emitterId, SseEmitterWrapper wrapper) {
    redisTemplate.opsForHash().put(emitterKey(userId), emitterId, wrapper);
    //log.info("Emitter 저장: {} -> {}", userId, wrapper.getEmitterId());
  }

  public Set<SseEmitterWrapper> findAllEmittersByUserId(UUID userId) {
    Map<Object, Object> entries = redisTemplate.opsForHash().entries(emitterKey(userId));
    return entries.values().stream()
        .filter(SseEmitterWrapper.class::isInstance)
        .map(SseEmitterWrapper.class::cast)
        .collect(Collectors.toSet());
  }

  public void delete(UUID userId, UUID emitterId) {
    redisTemplate.opsForHash().delete(emitterKey(userId), emitterId);
    log.info("Emitter 삭제: userId={}, emitterId={}", userId, emitterId);
  }

  public void saveEvent(UUID userId, SseEvent event) {
    redisTemplate.opsForList().rightPush(eventKey(userId), event);
    log.debug("이벤트 캐시 저장: userId={}, eventId={}", userId, event.id());
  }

  public List<SseEvent> findEventsAfter(UUID userId, UUID lastEventId) {
    List<Object> allEvents = redisTemplate.opsForList().range(eventKey(userId), 0, -1);
    boolean found = false;
    List<SseEvent> result = new ArrayList<>();
    for (Object obj : allEvents) {
      if (!(obj instanceof SseEvent event)) {
        continue;
      }
      if (found) {
        result.add(event);
      } else if (event.id().equals(lastEventId)) {
        found = true;
      }
    }
    return result;
    // List: 삽입 순서를 보장하므로, id가 lastEventId인 event를 기준으로 그 이후의 event만 추출 가능
  }

//  public Map<UUID, Set<SseEmitterWrapper>> findAllEmitters() {
//    return emitters;
//  }


}
