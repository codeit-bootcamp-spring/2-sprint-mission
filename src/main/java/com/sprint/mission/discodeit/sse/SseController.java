package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.sse.service.SseLocalService;
import com.sprint.mission.discodeit.util.SecurityUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sse")
public class SseController {

  private final SseLocalService sseLocalService;

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe(@RequestParam(required = false) UUID lastEventId) {
    log.info("Sse 연결 요청: {}", lastEventId);
    UUID userId = SecurityUtils.getCurrentUserId();
    return sseLocalService.subscribe(userId, lastEventId);
  }

}
