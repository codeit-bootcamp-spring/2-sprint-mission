package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.SseService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {

  private final SseService sseService;

  @GetMapping(path = "/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter addConnection(
      @PathVariable UUID userId,
      @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId
  ) {
    log.debug("[{}] SSE 연결 요청", userId);
    SseEmitter res = sseService.addConnection(userId, lastEventId);
    log.debug("SSE 연결 응답 : {}", res);
    return res;
  }
}
