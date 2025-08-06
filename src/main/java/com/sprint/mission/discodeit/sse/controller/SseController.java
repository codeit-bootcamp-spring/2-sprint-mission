package com.sprint.mission.discodeit.sse.controller;

import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.security.userDetails.CustomUserDetails;
import com.sprint.mission.discodeit.sse.repository.Emitter;
import com.sprint.mission.discodeit.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

  private static final String LAST_EVENT = "Last-Event-ID";

  private final SseService sseService;

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter connect(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestHeader(LAST_EVENT) String lastEventId
  ) {
    UserResult userResult = principal.getUserResult();
    Emitter connect = sseService.connect(userResult.id(), lastEventId);
    return connect.getSseEmitter();
  }

}




