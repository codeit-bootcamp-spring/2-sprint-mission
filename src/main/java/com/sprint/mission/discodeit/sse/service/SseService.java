package com.sprint.mission.discodeit.sse.service;

import com.sprint.mission.discodeit.sse.repository.ConnectionManager;
import com.sprint.mission.discodeit.sse.repository.Emitter;
import com.sprint.mission.discodeit.sse.repository.MissingEvent;
import de.huxhorn.sulky.ulid.ULID;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
public class SseService {

  private static final int MAX_CONNECTION_LIMIT = 10;
  private static final int MAX_BUFFER = 100;
  private static final ULID idGenerator = new ULID();

  private final Map<UUID, ConcurrentLinkedQueue<MissingEvent>> missingEvents = new ConcurrentHashMap<>();
  private final ConnectionManager connectionManager;

  public Emitter connect(UUID userId, String lastEventId) {
    Emitter savedEmitter = connectionManager.save(userId, MAX_CONNECTION_LIMIT);
    registerCallBack(userId, savedEmitter);
    sendEvent(userId, savedEmitter, "connect", "SSE connected");
    resendMissingEvents(userId, lastEventId, savedEmitter);

    return savedEmitter;
  }

  private void registerCallBack(UUID userId, Emitter newEmitter) {
    SseEmitter newEmitterSseEmitter = newEmitter.getSseEmitter();
    newEmitterSseEmitter.onCompletion(() -> connectionManager.delete(userId, newEmitter));
    newEmitterSseEmitter.onTimeout(() -> connectionManager.delete(userId, newEmitter));
    newEmitterSseEmitter.onError(e -> connectionManager.delete(userId, newEmitter));
  }

  public void broadcast(UUID userId, String name, Object data) {
    Set<Emitter> emitters = connectionManager.findUserEmitters(userId);
    for (Emitter emitter : emitters) {
      sendEvent(userId, emitter, name, data);
    }
  }

  @Scheduled(fixedRate = 3_600_000L)
  private void sendPing() {
    Set<Emitter> allEmitters = connectionManager.findAllEmitters()
        .orElseThrow(() -> new IllegalArgumentException("sse emiter가 없습니다."));
    for (Emitter sseEmitter : allEmitters) {
      sendEvent(null, sseEmitter, "ping", "Send ping");
    }
  }

  public void sendEvent(UUID userId, Emitter emitter, String name, Object data) {
    String messageId = idGenerator.nextULID();
    try {
      emitter.sendMessage(messageId, name, data);
    } catch (IOException e) {
      saveMissingEvent(userId, new MissingEvent(messageId, name, data));
      emitter.getSseEmitter().completeWithError(e);
    }
  }

  private void saveMissingEvent(UUID userId, MissingEvent event) {
    missingEvents.computeIfAbsent(userId, id -> new ConcurrentLinkedQueue<>());
    ConcurrentLinkedQueue<MissingEvent> queue = missingEvents.get(userId);

    // 오래된 이벤트 제거
    while (queue.size() >= MAX_BUFFER) {
      queue.poll();
    }

    queue.offer(event);
  }

  public void resendMissingEvents(UUID userId, String lastEventId, Emitter savedEmitter) {
    List<MissingEvent> missedEvents = missingEvents.getOrDefault(userId,
            new ConcurrentLinkedQueue<>())
        .stream()
        .dropWhile(e -> lastEventId != null && e.id().compareTo(lastEventId) <= 0)
        .toList();

    for (MissingEvent event : missedEvents) {
      try {
        savedEmitter.sendMessage(event.id(), event.messageName(), event.data());
      } catch (IOException ex) {
        savedEmitter.getSseEmitter().completeWithError(ex);
      }
    }
  }

}
