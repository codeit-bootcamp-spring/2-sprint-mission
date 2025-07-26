package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.domain.notification.dto.NotificationDto;
import com.sprint.mission.discodeit.domain.notification.entity.Notification;
import com.sprint.mission.discodeit.domain.notification.repository.NotificationRepository;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Service
public class SseEmitterService {

  private static final Long DEFAULT_TIMEOUT = 1000L * 60 * 30;
  private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

  private final NotificationRepository notificationRepository; // 유실된 이벤트 조회를 위해

  public SseEmitter subscribe(UUID userId, String lastEventId) {
    SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
    emitters.putIfAbsent(userId, new CopyOnWriteArrayList<>());
    emitters.get(userId).add(sseEmitter);

    log.info("새로운 SSE Emitter 연결: userId={}, emitter={}", userId, sseEmitter);

    sseEmitter.onCompletion(() -> removeEmitter(userId, sseEmitter, "onCompletion"));
    sseEmitter.onTimeout(() -> removeEmitter(userId, sseEmitter, "onTimeout"));
    sseEmitter.onError(e -> removeEmitter(userId, sseEmitter, "onError: " + e.getMessage()));

    String eventId = "0_" + UUID.randomUUID().toString();
    sendToEmitter(sseEmitter, eventId, "sse-connection",
        "SSE connection successful. [userId=" + userId + "]");

    // Last-Event-ID가 있다면 유실된 데이터 전송
    if (lastEventId != null && !lastEventId.isEmpty()) {
      sendLostData(userId, lastEventId, sseEmitter);
    }

    return sseEmitter;
  }

  public void sendNotification(UUID userId, NotificationDto notification) {
    List<SseEmitter> userEmitters = emitters.get(userId);
    if (userEmitters == null || userEmitters.isEmpty()) {
      log.warn("사용자에게 연결된 Emitter가 없습니다: userId={}", userId);
      return;
    }
    log.info("사용자에게 알림 전송 시도: userId={}, Emitter 개수={}", userId, userEmitters.size());

    String eventId = notification.createdAt().toEpochMilli() + "_" + notification.id().toString();
    String eventName = "notification";

    userEmitters.forEach(emitter -> {
      log.debug("알림 전송 시도: userId={}, eventId={}", userId, eventId);
      sendToEmitter(emitter, eventId, eventName, notification);
    });
  }

  //15초마다 실행
  @Scheduled(fixedRate = 15000)
  public void sendPing() {
    emitters.forEach((userId, userEmitters) -> {
      userEmitters.forEach(emitter -> {
        try {
          emitter.send(SseEmitter.event().comment("ping"));
          log.info("Ping 전송 성공: userId={}", userId);
        } catch (IOException e) {
          log.warn("Ping 전송 실패, Emitter 제거: userId={}, error={}", userId, e.getMessage());
          removeEmitter(userId, emitter, "ping failed");
        }
      });
    });
  }

  private void sendLostData(UUID userId, String lastEventId, SseEmitter emitter) {
    // lastEventId 파싱
    String[] parts = lastEventId.split("_");
    if (parts.length < 1) {
      log.warn("잘못된 형식의 Last-Event-ID 입니다: {}", lastEventId);
      return;
    }
    try {
      long lastEventTimestamp = Long.parseLong(parts[0]);
      Instant after = Instant.ofEpochMilli(lastEventTimestamp);

      List<Notification> lostNotifications = notificationRepository
          .findByReceiverIdAndCreatedAtAfterOrderByCreatedAtAsc(userId, after);

      if (!lostNotifications.isEmpty()) {
        log.info("유실된 데이터 전송 시작: userId={}, lastEventId={}, count={}", userId, lastEventId,
            lostNotifications.size());
        for (Notification notification : lostNotifications) {
          if (notification.getReceiverId().equals(userId)) {
            sendToEmitter(emitter, notification.getId().toString(), "notification",
                NotificationDto.from(notification));
          }
        }
        log.info("유실된 데이터 전송 완료.");
      }
    } catch (Exception e) {
      log.error("유실된 데이터 처리 중 오류 발생: lastEventId={}, error={}", lastEventId, e.getMessage(), e);
    }
  }

  private void sendToEmitter(SseEmitter emitter, String eventId, String eventName, Object data) {
    try {
      log.info("SSE 이벤트 전송중...");
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name(eventName)
          .data(data));
      log.info("SSE 이벤트 전송완료...");
    } catch (IOException e) {
      log.error("SSE 이벤트 전송 실패: eventId={}, eventName={}, error={}", eventId, eventName,
          e.getMessage());
      emitter.complete();
    }
  }

  private void removeEmitter(UUID userId, SseEmitter emitter, String reason) {
    List<SseEmitter> emitterList = emitters.get(userId);
    if (emitterList != null) {
      boolean removed = emitterList.remove(emitter);
      if (removed) {
        log.info("Emitter 제거 완료: userId={}, reason={}, remainingEmitters={}", userId, reason,
            emitterList.size());
        if (emitterList.isEmpty()) {
          emitters.remove(userId);
          log.info("사용자 Emitter 리스트가 비어있어 맵에서 제거: userId={}", userId);
        }
      }
    }
  }
}
