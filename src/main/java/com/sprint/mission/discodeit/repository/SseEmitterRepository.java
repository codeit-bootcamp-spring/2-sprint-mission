package com.sprint.mission.discodeit.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
@Slf4j
public class SseEmitterRepository {

  private final Map<UUID, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

  public SseEmitter save(UUID userId, SseEmitter emitter) {
    emitters.computeIfAbsent(userId, key -> new CopyOnWriteArrayList<>()).add(emitter);
    log.debug("{} 에 새로운 emitter 추가됨", userId);
    return emitter;
  }

  public void delete(UUID userId, SseEmitter emitter) {
    if (emitters.containsKey(userId)) {
      List<SseEmitter> list = emitters.get(userId);
      list.remove(emitter);
      log.debug("{} 의 Emitter가 삭제됨", userId);
      if (list.isEmpty()) {
        emitters.remove(userId);
      }
    }
  }

  public List<SseEmitter> findByUserId(UUID userId) {
    if (emitters.containsKey(userId)) {
      return emitters.get(userId);
    }
    return null;
  }

  public Map<UUID, List<SseEmitter>> findAllEmitters() {
    Map<UUID, List<SseEmitter>> map = new HashMap<>();
    emitters.forEach((userId, emitters) ->
        map.put(userId, new ArrayList<>(emitters)));

    return map;
  }

}
