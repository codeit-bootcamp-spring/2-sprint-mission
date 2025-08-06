package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.repository.SseRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SseController {

  private final SseRepository sseRepository;
  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;

  @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public ResponseEntity<SseEmitter> subscribe(
      @AuthenticationPrincipal DiscodeitUserDetails userDetail,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

    String userId = userDetail.getUserDto().id().toString();
    String emitterId = userId + "-" + System.currentTimeMillis();
    SseEmitter emitter = sseRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

    emitter.onCompletion(() -> sseRepository.deleteById(emitterId));
    emitter.onTimeout(() -> sseRepository.deleteById(emitterId));
    emitter.onError(e -> {
      log.error("SSE 에러 발생 : id={}", emitterId, e);
      sseRepository.deleteById(emitterId);
    });

    sendToClient(emitter, emitterId, "connected",
        "SSE connected successfully. [userId=]" + userId + "]");

    if (!lastEventId.isEmpty()) {
      Map<String, Object> events = sseRepository.findAllEventCacheStartWithByUserId(userId);
      events.entrySet().stream()
          .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
          .forEach(
              entry -> sendToClient(emitter, entry.getKey(), "notification", entry.getValue()));
    }

    return ResponseEntity.ok(emitter);

  }

  private void sendToClient(SseEmitter emitter, String emitterId, String name, Object data) {
    try {
      emitter.send(SseEmitter.event()
          .id(emitterId)
          .name(name)
          .data(data));
    } catch (IOException e) {
      log.error("SSE 이벤트 송신 중 에러 발생 : id={}", emitterId, e);
      emitter.completeWithError(e);
    }
  }

}
