package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseController {
    private final SseService sseService;

    @GetMapping("/api/sse")
    public SseEmitter connect(
            @RequestHeader("Request-User-ID") String userId,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId
    ) {
        return sseService.connect(userId, lastEventId);
    }
}