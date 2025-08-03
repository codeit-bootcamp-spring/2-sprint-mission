package com.sprint.mission.discodeit.sse.service;

import com.sprint.mission.discodeit.sse.SseEmitterWrapper;
import com.sprint.mission.discodeit.sse.SseEvent;
import com.sprint.mission.discodeit.sse.repository.EmitterRedisRepository;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Component
public class SseRedisService {

  private static final Long INACTIVE_TIMEOUT_MINUTES = 60L * 1000 * 10;
  //인터페이스로 바꿀 것
  private final EmitterRedisRepository emitterRepository;

  public void sendEventToUser(UUID userId, SseEvent sseEvent) {
    Set<SseEmitterWrapper> emitterWrappers = emitterRepository.findAllEmittersByUserId(userId);
    if (emitterWrappers.isEmpty()) {
      log.info("No active emitters for user {}", userId);
      return;
    }

    emitterRepository.saveEvent(userId, sseEvent);

    emitterWrappers.forEach(wrapper -> sendEventToEmitter(userId, sseEvent));
  }

  public void sendEventToEmitter(UUID userId, SseEvent sseEvent) {
    Set<SseEmitterWrapper> emitters = emitterRepository.findAllEmittersByUserId(userId);
    if (emitters.isEmpty()) {
      return;
    }

    emitterRepository.saveEvent(userId, sseEvent);

    for (SseEmitterWrapper wrapper : emitters) {
      try {
        wrapper.getEmitter().send(
            SseEmitter.event()
                .id(sseEvent.id().toString())
                .name(sseEvent.name())
                .data(sseEvent.data())
        );
        wrapper.updateLastActiveAt();
        log.info("SSE 전송 완료: userId={}, emitterId={}", wrapper.getEmitterId(), sseEvent.id());
      } catch (IOException e) {
        emitterRepository.delete(userId, wrapper.getEmitterId());
        log.warn("SSE 전송 실패, emitter 삭제: emitterId={}, error: {}", wrapper.getEmitterId(),
            e.getMessage());
      }
    }
  }

//  @Scheduled(fixedRate = 300000) // 5분마다 실행
//  public void cleanupInactiveConnections() {
//    log.debug("비활성 연결 정리 작업 시작");
//    emitterRepository.findAllEmitters().forEach((userId, wrappers) -> {
//      wrappers.forEach(wrapper -> {
//        try {
//          // 하트비트 전송
//          wrapper.getEmitter().send(SseEmitter.event().name("heartbeat").data("ping"));
//          wrapper.updateLastActiveAt();
//        } catch (IOException e) {
//          // 하트비트 전송 실패는 연결이 끊어진 것으로 간주하고 삭제
//          emitterRepository.delete(userId, wrapper.getEmitterId());
//          log.info("비활성 연결 정리: userId={}, emitterId={}", userId, wrapper.getEmitterId());
//        }
//      });
//    });
//  }

}
