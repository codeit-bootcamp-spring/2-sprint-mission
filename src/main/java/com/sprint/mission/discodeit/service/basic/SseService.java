package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.SseRepository;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

  private final SseRepository sseRepository;

  public void sendEventToUser(UUID userId, String eventName, Object data) {
    // 해당 사용자의 모든 활성 Emitter를 조회
    Map<String, SseEmitter> emitters = sseRepository.findAllEmitterStartWithByUserId(
        userId.toString());

    emitters.forEach((emitterId, emitter) -> {
      // 유실 복구를 위해 이벤트 캐시 저장
      sseRepository.saveEventCache(emitterId, data);

      try {
        emitter.send(SseEmitter.event()
            .id(emitterId)
            .name(eventName)
            .data(data));
        log.info("SSE 이벤트 전송 성공: userId={}, eventName={}, emitterId={}", userId, eventName,
            emitterId);

        // 전송 성공 후 캐시에서 해당 이벤트 제거
        sseRepository.deleteEventCacheById(emitterId);
      } catch (Exception e) {
        log.warn("SSE 이벤트 전송 실패, Emitter를 제거합니다: emitterId={}, eventName={}", emitterId, eventName,
            e);
        sseRepository.deleteById(emitterId);
      }
    });
  }

  public void broadcastEvent(String eventName, Object data) {
    log.info("SSE 브로드캐스트 시작: eventName={}", eventName);
    Map<String, SseEmitter> emitters = sseRepository.findAll();
    emitters.forEach((emitterId, emitter) -> {
      try {
        // 브래드캐스트 이벤트는 캐시 저장 X
        emitter.send(SseEmitter.event()
            .id(emitterId)
            .name(eventName)
            .data(data));
      } catch (Exception e) {
        log.warn("SSE 브로드캐스트 중 예외 발생, Emitter를 제거합니다: emitterId={}, eventName={}", emitterId,
            eventName, e);
        sseRepository.deleteById(emitterId);
      }
    });
    log.info("SSE 브로드캐스트 완료: eventName={}, 대상 Emitter 수={}", eventName, emitters.size());
  }
}
