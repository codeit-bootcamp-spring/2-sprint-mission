package com.sprint.mission.discodeit.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Repository
public class SseRepository {

  // key : emitterId
  private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
  // 이벤트 유실을 방지하기 위한 이벤트 캐시
  // key : emitterId, value : 전송해야 할 이벤트 객체
  private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

  // 새로운 emitter 저장
  public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
    emitters.put(emitterId, sseEmitter);
    log.info("새로운 Emitter 저장: emitterId={}", emitterId);
    return sseEmitter;
  }

  // 이벤트 유실을 대비해 캐시에 저장
  public void saveEventCache(String emitterId, Object event) {
    eventCache.put(emitterId, event);
    log.debug("이벤트 캐시 저장: emitterId={}, event={}", emitterId, event);
  }

  // 특정 사용자의 모든 emitter 를 조회
  public Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId) {
    return emitters.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(userId))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  // 특정 사용자에게 전송된 모든 이벤트 캐시 조회
  public Map<String, Object> findAllEventCacheStartWithByUserId(String userId) {
    return eventCache.entrySet().stream()
        .filter(entry -> entry.getKey().startsWith(userId))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  public Map<String, SseEmitter> findAll() {
    return this.emitters;
  }

  // 특정 emitter 제거
  public void deleteById(String emitterId) {
    emitters.remove(emitterId);
    log.info("Emitter 삭제: emitterId={}", emitterId);
  }

  // 특정 사용자에게 전송된 모든 이벤트 캐시 제거
  public void deleteAllEventCacheStartWithId(String userId) {
    eventCache.keySet().removeIf(key -> key.startsWith(userId));
    log.info("이벤트 캐시 삭제: userId={}", userId);
  }

  public void deleteEventCacheById(String emitterId) {
    eventCache.remove(emitterId);
    log.debug("이벤트 캐시 삭제: emitterId={}", emitterId);
  }


}
