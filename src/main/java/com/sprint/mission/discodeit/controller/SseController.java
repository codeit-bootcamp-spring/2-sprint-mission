package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.SseEmitterService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/sse")
public class SseController {

    private final SseEmitterService sseEmitterService;

    @GetMapping
    public SseEmitter connect(
        @AuthenticationPrincipal DiscodeitUserDetails userDetails,
        @RequestHeader(value = "Last-Event-ID", required = false) String lastEvnetId
    ) {
        UUID userId = userDetails.getUserDto().id();

        // 유저의 SSE 연결 생성 및 emitter 객체 반환
        return sseEmitterService.connect(userId, lastEvnetId);
    }
}
