package com.sprint.mission.discodeit.sse.repository;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Component
public class ConnectionManager {

  private final Map<UUID, ConcurrentSkipListSet<Emitter>> emitters = new ConcurrentHashMap<>();

  public Emitter save(UUID userId, int maxConnectionLimit) {
    Emitter newEmitter = new Emitter(new SseEmitter(60 * 60 * 1000L));
    emitters.computeIfAbsent(userId, k -> new ConcurrentSkipListSet<>(
        Comparator.comparing(Emitter::getCreatedAt))
    ).add(newEmitter);

    if (emitters.get(userId).size() > maxConnectionLimit) {
      ConcurrentSkipListSet<Emitter> priorityEmitters = emitters.get(userId);
      priorityEmitters.pollFirst();
    }

    return newEmitter;
  }

  public Set<Emitter> findUserEmitters(UUID userId) {
    ConcurrentSkipListSet<Emitter> emitters = this.emitters.get(userId);
    if (emitters == null) {
      throw new IllegalArgumentException("해당 user-id의 emiiter가 없습니다.");
    }

    return emitters;
  }

  public Set<Emitter> findAllEmitters() {
    return emitters.values()
        .stream()
        .flatMap(ConcurrentSkipListSet::stream)
        .collect(Collectors.toSet());
  }

  public void delete(UUID userId, Emitter emitter) {
    ConcurrentSkipListSet<Emitter> savedEmitters = emitters.get(userId);
    if (savedEmitters == null) {
      return;
    }
    savedEmitters.remove(emitter);
    if (savedEmitters.isEmpty()) {
      emitters.remove(userId);
    }
  }

}
