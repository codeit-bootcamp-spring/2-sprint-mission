package com.sprint.mission.discodeit.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
public class SseService {
  // ConcurrentHashMap -> 여러 스레드가 동시에 접근해도 안전한 Map
  // List<SseEmitter> -> 사용자 한명이 여러 탭 기기에서 접속하는 것을 지원
  private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

  public SseEmitter subscribe(UUID userId, String lastEventId) {
    // SseEmitter 객체 생성 (유효기간 10분)
    SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);

    // 스레드 세이프한 리스트에 emitter 추가
    emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

    // 메모리 누수 방지 처리
    emitter.onCompletion(() -> removeEmitter(userId, emitter));
    emitter.onTimeout(() -> removeEmitter(userId, emitter));
    emitter.onError(e -> removeEmitter(userId, emitter));

    // 연결 성공 및 유실된 이벤트 재전송
    sendToEmitter(emitter, "sse-connected", "SSE connection successful", userId.toString());

    return emitter;
  }

  public void send(UUID userId, String eventName, Object data) {
    List<SseEmitter> userEmitters = emitters.get(userId);
    if (userEmitters != null) {
      userEmitters.forEach(emitter -> sendToEmitter(emitter, eventName, data, UUID.randomUUID().toString()));
    }
  }

  private void sendToEmitter(SseEmitter emitter, String eventName, Object data, String eventId) {
    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name(eventName)
          .data(data));
    } catch (IOException e) {
      log.error("SSE event sending error: {}", e.getMessage());
      emitter.completeWithError(e);
    }
  }

  private void removeEmitter(UUID userId, SseEmitter emitter) {
    List<SseEmitter> userEmitters = emitters.get(userId);
    if (userEmitters != null) {
      userEmitters.remove(emitter);
      if (userEmitters.isEmpty()) {
        emitters.remove(userId);
      }
    }
  }
}
