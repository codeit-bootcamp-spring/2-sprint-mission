package com.sprint.mission.discodeit.sse.service;

import com.sprint.mission.discodeit.sse.SseEmitterWrapper;
import com.sprint.mission.discodeit.sse.SseEvent;
import com.sprint.mission.discodeit.sse.repository.EmitterLocalRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Component
public class SseLocalService {

  private static final Long INACTIVE_TIMEOUT_MINUTES = 60L * 1000 * 10;
  private final EmitterLocalRepository emitterLocalRepository;

  public SseEmitter subscribe(UUID userId, UUID lastEventId) {
    SseEmitterWrapper wrapper = new SseEmitterWrapper(new SseEmitter(INACTIVE_TIMEOUT_MINUTES));
    SseEmitter emitter = wrapper.getEmitter();
    UUID emitterId = wrapper.getEmitterId();

    emitterLocalRepository.save(userId, wrapper);

    // 타임아웃, 완료, 에러 발생 시 특정 Emitter만 삭제
    emitter.onCompletion(() -> emitterLocalRepository.delete(userId, emitterId));
    emitter.onTimeout(() -> emitterLocalRepository.delete(userId, emitterId));
    emitter.onError(e -> {
      log.error("SSE Error for userId={}, emitterId={}: {}", userId, emitterId, e.getMessage());
      emitterLocalRepository.delete(userId, emitterId);
    });

    // 503 에러 방지를 위한 초기 연결 이벤트 전송
    SseEvent sseEvent = new SseEvent(
        UUID.randomUUID(),
        "connect",
        Map.of("Connection established", "Connection established")
    );
    sendEventToEmitter(userId, wrapper, sseEvent);

    // 유실된 이벤트 복원 처리
    if (lastEventId != null) {
      try {
        List<SseEvent> lostEvents = emitterLocalRepository.findEventsAfter(userId, lastEventId);
        lostEvents.forEach(
            event -> sendEventToEmitter(userId, wrapper, event));
      } catch (IllegalArgumentException e) {
        log.warn("Invalid Last-Event-ID format: {}", lastEventId);
      }
    }

    return emitter;
  }

  public void sendEventToUser(UUID userId, SseEvent sseEvent) {
    Set<SseEmitterWrapper> emitterWrappers = emitterLocalRepository.findAllEmittersByUserId(userId);
    if (emitterWrappers.isEmpty()) {
      log.info("No active emitters for user {}", userId);
      return;
    }

    emitterLocalRepository.saveEvent(userId, sseEvent);

    emitterWrappers.forEach(wrapper -> sendEventToEmitter(userId, wrapper, sseEvent));
  }

  /**
   * 특정 Emitter에 이벤트를 전송
   */
  private void sendEventToEmitter(UUID userId, SseEmitterWrapper wrapper, SseEvent sseEvent) {
    SseEmitter emitter = wrapper.getEmitter();
    try {
      SseEmitter.SseEventBuilder builder = SseEmitter.event()
          .id(sseEvent.id().toString())
          .name(sseEvent.name())
          .data(sseEvent.data());
      emitter.send(builder);
      wrapper.updateLastActiveAt();
      log.info("SSE 전송 완료: userId={}, emitterId={}", wrapper.getEmitterId(), sseEvent.id());
    } catch (IOException e) {
      // 전송 실패 시 해당 Emitter 삭제
      emitterLocalRepository.delete(userId, wrapper.getEmitterId());
      log.warn("SSE 전송 실패, emitter 삭제: emitterId={}, error: {}", wrapper.getEmitterId(),
          e.getMessage());
    }
  }

  @Scheduled(fixedRate = 300000) // 5분마다 실행
  public void cleanupInactiveConnections() {
    log.debug("비활성 연결 정리 작업 시작");
    emitterLocalRepository.findAllEmitters().forEach((userId, wrappers) -> {
      wrappers.forEach(wrapper -> {
        try {
          // 하트비트 전송
          wrapper.getEmitter().send(SseEmitter.event().name("heartbeat").data("ping"));
          wrapper.updateLastActiveAt();
        } catch (IOException e) {
          // 하트비트 전송 실패는 연결이 끊어진 것으로 간주하고 삭제
          emitterLocalRepository.delete(userId, wrapper.getEmitterId());
          log.info("비활성 연결 정리: userId={}, emitterId={}", userId, wrapper.getEmitterId());
        }
      });
    });
  }

}
