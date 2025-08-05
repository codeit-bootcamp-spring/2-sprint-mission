package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.*;


@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@RequestHeader("X-USER-ID") String userId,
        @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {
        return sseService.connect(userId, lastEventId);
    }
}
