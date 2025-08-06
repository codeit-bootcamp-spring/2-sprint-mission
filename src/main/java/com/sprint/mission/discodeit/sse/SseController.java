package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.domain.auth.entity.DiscodeitUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  private final SseEmitterService sseEmitterService;

  @GetMapping(value = "/sse")
  public ResponseEntity<SseEmitter> subscribe(
      @AuthenticationPrincipal DiscodeitUserDetails principal,
      @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId
  ) {
    UUID userId = principal.getUserDto().id();
    log.info("SSE 연결 요청: userId={}, Last-Event-ID={}", userId, lastEventId);
    SseEmitter emitter = sseEmitterService.subscribe(userId, lastEventId);
    return ResponseEntity.ok(emitter);
  }
}
