package com.sprint.mission.discodeit.scheduler;

import com.sprint.mission.discodeit.repository.SseRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseScheduler {

  private final SseRepository sseRepository;

  @Scheduled(fixedRate = 60000)
  public void sendHeartbeat() {
    // 모든 활성 Emitter에 heartbeat 전송
    sseRepository.findAll().forEach((emitterId, emitter) -> {
      try {
        // heartbeat 라는 주석을 보내 연결 유지
        emitter.send(SseEmitter.event().comment("heartbeat"));
        log.trace("Sent heartbeat to emitterId: {}", emitterId);
      } catch (IOException e) {
        log.error("Client connection lost. Removing emitterId: id={}", emitterId, e);
        sseRepository.deleteById(emitterId);
      }
    });
  }
}
