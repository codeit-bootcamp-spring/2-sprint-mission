package com.sprint.mission.discodeit.sse.repository;

import com.sprint.mission.discodeit.sse.SseEmitterWrapper;
import com.sprint.mission.discodeit.sse.SseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class EmitterLocalRepository {

  // 모든 Emitter를 저장하는 스레드 세이프한 Map
  private final Map<UUID, Set<SseEmitterWrapper>> emitters = new ConcurrentHashMap<>();
  // 유실된 이벤트를 저장하는 스레드 세이프한 Map
  private final Map<UUID, List<SseEvent>> eventCache = new ConcurrentHashMap<>();

  public void save(UUID userId, SseEmitterWrapper wrapper) {
    emitters.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(wrapper);
    log.info("Emitter 저장: {} -> {}", userId, wrapper.getEmitterId());
  }

  public Set<SseEmitterWrapper> findAllEmittersByUserId(UUID userId) {
    return emitters.getOrDefault(userId, Collections.emptySet());
  }

  public void delete(UUID userId, UUID emitterId) {
    emitters.computeIfPresent(userId, (key, wrappers) -> {
      wrappers.removeIf(wrapper -> wrapper.getEmitterId().equals(emitterId));
      log.info("Emitter 삭제: userId={}, emitterId={}", userId, emitterId);
      if (wrappers.isEmpty()) {
        log.info("사용자 {}의 모든 Emitter가 삭제되었습니다.", userId);
        return null; // 키 제거
      }
      return wrappers; // 키 유지
    });
  }

  public void saveEvent(UUID userId, SseEvent event) {
    eventCache.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(event);
    log.debug("이벤트 캐시 저장: userId={}, eventId={}", userId, event.id());
  }

  public List<SseEvent> findEventsAfter(UUID userId, UUID lastEventId) {
    return eventCache.getOrDefault(userId, Collections.emptyList())
        .stream()
        .dropWhile(event -> !event.id().equals(lastEventId))
        .skip(1)
        .toList();

    // List: 삽입 순서를 보장하므로, id가 lastEventId인 event를 기준으로 그 이후의 event만 추출 가능

  }

  public Map<UUID, Set<SseEmitterWrapper>> findAllEmitters() {
    return emitters;
  }


}
