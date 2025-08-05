package com.sprint.mission.discodeit.repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Repository
public class EmitterRepository {
  private final Map<UUID, CopyOnWriteArrayList<SseEmitter>> userEmitters = new ConcurrentHashMap<>();

  public SseEmitter save(UUID userId, SseEmitter emitter) {
    CopyOnWriteArrayList<SseEmitter> emitterList = userEmitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>());
    emitterList.add(emitter);

    emitter.onCompletion(() -> {
      log.info("onCompletion callback for userId={}", userId);
      this.remove(userId, emitter);
    });

    emitter.onTimeout(() -> {
      log.info("onTimeout callback for userId={}", userId);
      emitter.complete();
    });

    emitter.onError(e -> {
      log.error("onError callback for userId={}", userId, e);
      this.remove(userId, emitter);
    });

    return emitter;
  }

  private void remove(UUID userId, SseEmitter emitter) {
    CopyOnWriteArrayList<SseEmitter> emitterList = userEmitters.get(userId);
    if (emitterList != null) {
      emitterList.remove(emitter);
      if (emitterList.isEmpty()) {
        userEmitters.remove(userId);
        log.info("No emitters left for userId={}, removing user from map.", userId);
      }
    }
  }

  public Optional<CopyOnWriteArrayList<SseEmitter>> get(UUID userId) {
    return Optional.ofNullable(userEmitters.get(userId));
  }


  public Map<UUID, CopyOnWriteArrayList<SseEmitter>> getAllEmitters() {
    return userEmitters;
  }


}
