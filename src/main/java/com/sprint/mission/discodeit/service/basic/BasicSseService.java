package com.sprint.mission.discodeit.service.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.event.sse.SseStoredEvent;
import com.sprint.mission.discodeit.listener.sse.SseEventIdGenerator;
import com.sprint.mission.discodeit.repository.SseEventStore;
import com.sprint.mission.discodeit.service.SseService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicSseService implements SseService {

  private static final Long DEFAULT_TIMEOUT = 60 * 60 * 1000L;
  private static final Long RECONNECT_TIME = 5000L;

  private final Map<UUID, CopyOnWriteArrayList<SseEmitterWrapper>> emitterMap = new ConcurrentHashMap<>();

  private final ObjectMapper objectMapper;
  private final SseEventStore eventStore;
  private final SseEventIdGenerator idGenerator;

  @Override
  public SseEmitter connect(UUID userId, String lastEventId) {
    log.info("SSE 연결 요청: userId={}, lastEventId={}", userId, lastEventId);

    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    String emitterId = UUID.randomUUID().toString();

    CopyOnWriteArrayList<SseEmitterWrapper> emitters = emitterMap.computeIfAbsent(
        userId, k -> new CopyOnWriteArrayList<>()
    );

    SseEmitterWrapper wrapper = new SseEmitterWrapper(emitterId, emitter, userId);
    emitters.add(wrapper);

    emitter.onCompletion(() -> removeEmitter(userId, wrapper));
    emitter.onTimeout(() -> {
      log.warn("SSE 연결 타임아웃: userId={}, emitterId={}", userId, emitterId);
      removeEmitter(userId, wrapper);
    });
    emitter.onError((ex) -> {
      log.error("SSE 연결 에러: userId={}, emitterId={}", userId, emitterId, ex);
      removeEmitter(userId, wrapper);
    });

    try {
      String eventId = idGenerator.generateEventId();
      SseEmitter.SseEventBuilder event = SseEmitter.event()
          .id(eventId)
          .name("connected")
          .data(Map.of("timestamp", LocalDateTime.now().toString()))
          .reconnectTime(RECONNECT_TIME);

      emitter.send(event);
      log.info("SSE 연결 성공: userId={}, emitterId={}", userId, emitterId);
    } catch (IOException e) {
      log.error("초기 연결 이벤트 전송 실패: userId={}, emitterId={}", userId, emitterId, e);
      removeEmitter(userId, wrapper);
    }

    if (lastEventId != null && !lastEventId.isEmpty()) {
      log.debug("이벤트 복원 시작: userId={}, lastEventId={}", userId, lastEventId);
      replayMissedEvents(emitter, userId, lastEventId);
    }

    return emitter;
  }

  @Override
  public void sendNotificationEvent(UUID userId, NotificationDto notification) {
    sendEvent(userId, "notifications", notification);
  }

  @Override
  public void sendBinaryContentStatusEvent(UUID userId, BinaryContentDto binaryContent) {
    sendEvent(userId, "binaryContents.status", binaryContent);
  }

  @Override
  public void sendChannelRefreshEvent(UUID userId, UUID channelId) {
    sendEvent(userId, "channels.refresh", Map.of("channelId", channelId.toString()));
  }

  @Override
  public void sendUserRefreshEvent(UUID userId, UUID targetUserId) {
    sendEvent(userId, "users.refresh", Map.of("userId", targetUserId.toString()));
  }

  @Override
  public void disconnect(UUID userId) {
    log.info("사용자 SSE 연결 종료: userId={}", userId);
    CopyOnWriteArrayList<SseEmitterWrapper> emitters = emitterMap.remove(userId);
    if (emitters != null) {
      emitters.forEach(wrapper -> {
        try {
          wrapper.emitter.complete();
        } catch (Exception e) {
          log.error("SSE 연결 종료 중 에러: userId={}, emitterId={}", userId, wrapper.id, e);
        }
      });
    }
  }

  @Scheduled(fixedDelay = 30000)
  public void sendPingEvents() {
    emitterMap.forEach((userId, emitters) -> {
      emitters.forEach(wrapper -> {
        try {
          String eventId = idGenerator.generateEventId();
          SseEmitter.SseEventBuilder event = SseEmitter.event()
              .id(eventId)
              .name("ping")
              .data(Map.of("timestamp", LocalDateTime.now().toString()));

          wrapper.emitter.send(event);
          wrapper.lastPingTime = System.currentTimeMillis();
        } catch (Exception e) {
          log.warn("Ping 전송 실패, 연결 제거: userId={}, emitterId={}", userId, wrapper.id);
          removeEmitter(userId, wrapper);
        }
      });
    });
  }

  @Scheduled(fixedDelay = 60000)
  public void cleanupDeadConnections() {
    long now = System.currentTimeMillis();
    long timeout = 90000;

    emitterMap.forEach((userId, emitters) -> {
      emitters.removeIf(wrapper -> {
        if (now - wrapper.lastPingTime > timeout) {
          log.warn("응답 없는 연결 제거: userId={}, emitterId={}", userId, wrapper.id);
          try {
            wrapper.emitter.complete();
          } catch (Exception e) {
          }
          return true;
        }
        return false;
      });

      if (emitters.isEmpty()) {
        emitterMap.remove(userId);
      }
    });
  }

  private void sendEvent(UUID userId, String eventName, Object data) {
    CopyOnWriteArrayList<SseEmitterWrapper> emitters = emitterMap.get(userId);
    if (emitters == null || emitters.isEmpty()) {
      log.debug("연결된 SSE 클라이언트 없음: userId={}", userId);
      return;
    }

    String eventId = idGenerator.generateEventId();

    emitters.forEach(wrapper -> {
      try {
        String jsonData = objectMapper.writeValueAsString(data);

        SseEmitter.SseEventBuilder event = SseEmitter.event()
            .id(eventId)
            .name(eventName)
            .data(jsonData);

        wrapper.emitter.send(event);
        log.debug("SSE 이벤트 전송 성공: userId={}, eventName={}, eventId={}",
            userId, eventName, eventId);
      } catch (Exception e) {
        log.error("SSE 이벤트 전송 실패: userId={}, eventName={}", userId, eventName, e);
        removeEmitter(userId, wrapper);
      }
    });
  }

  private void replayMissedEvents(SseEmitter emitter, UUID userId, String lastEventId) {
    try {
      List<SseStoredEvent> missedEvents = eventStore.getEventsSince(userId, lastEventId);

      log.info("놓친 이벤트 재전송 시작: userId={}, count={}", userId, missedEvents.size());

      for (SseStoredEvent event : missedEvents) {
        try {
          SseEmitter.SseEventBuilder sseEvent = SseEmitter.event()
              .id(event.eventId())
              .name(event.eventType())
              .data(event.data());

          emitter.send(sseEvent);
          log.debug("놓친 이벤트 재전송 성공: eventId={}, eventType={}",
              event.eventId(), event.eventType());
        } catch (IOException e) {
          log.warn("놓친 이벤트 재전송 실패: eventId={}", event.eventId(), e);
        }
      }

      log.info("놓친 이벤트 재전송 완료: userId={}", userId);

    } catch (Exception e) {
      log.error("놓친 이벤트 조회 실패: userId={}, lastEventId={}", userId, lastEventId, e);
    }
  }

  private void removeEmitter(UUID userId, SseEmitterWrapper wrapper) {
    CopyOnWriteArrayList<SseEmitterWrapper> emitters = emitterMap.get(userId);
    if (emitters != null) {
      emitters.remove(wrapper);
      if (emitters.isEmpty()) {
        emitterMap.remove(userId);
      }
    }
    log.debug("SSE 연결 제거: userId={}, emitterId={}", userId, wrapper.id);
  }

  private static class SseEmitterWrapper {

    final String id;
    final SseEmitter emitter;
    final UUID userId;
    long lastPingTime;

    SseEmitterWrapper(String id, SseEmitter emitter, UUID userId) {
      this.id = id;
      this.emitter = emitter;
      this.userId = userId;
      this.lastPingTime = System.currentTimeMillis();
    }
  }
}