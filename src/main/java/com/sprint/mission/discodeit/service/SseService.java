package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.dto.data.SseEvent;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.SseEmitterRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseService {

  private final SseEmitterRepository sseEmitterRepository;
  private final UserRepository userRepository;

  private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
  private final ConcurrentHashMap<UUID, List<SseEvent>> userEvents = new ConcurrentHashMap<>();
  private final AtomicLong eventIdCounter = new AtomicLong(System.currentTimeMillis());

  public SseEmitter addConnection(UUID userId, String lastEventId) {
    SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
    sseEmitterRepository.save(userId, emitter);

    emitter.onCompletion(() -> {
      sseEmitterRepository.delete(userId, emitter);
      log.debug("SSE 연결이 완료됨 userId: {}", userId);
    });
    emitter.onTimeout(() -> {
      sseEmitterRepository.delete(userId, emitter);
      log.debug("SSE 연결 시간이 초과됨 userId: {}", userId);
    });
    emitter.onError(error -> {
      sseEmitterRepository.delete(userId, emitter);
      log.error("SSE 연결에 에러가 발생함 userId: {}", userId, error);
    });

    sendToClient(userId, emitter, "connect", "SSE connected");

    if (lastEventId != null && !lastEventId.isEmpty()) {
      try {
        long lastId = Long.parseLong(lastEventId);
        List<SseEvent> missedEvents = userEvents.getOrDefault(userId,
                new CopyOnWriteArrayList<>()).stream()
            .filter(event -> {
              try {
                return Long.parseLong(event.getId()) > lastId;
              } catch (NumberFormatException e) {
                return false;
              }
            }).toList();

        log.debug("[{}] 마지막 이벤트 ID({}) 이후 놓친 이벤트를 복구. 개수: {}", userId,
            lastEventId, missedEvents.size());
        for (SseEvent event : missedEvents) {
          sendToClient(userId, emitter, event.getId(), event.getName(), event.getData());
        }
      } catch (NumberFormatException e) {
        log.error("잘못된 Last-Event-ID 형식 오류: {}", lastEventId, e);
      }
    }

    return emitter;
  }

  public void sendNotification(UUID receiverId, NotificationDto notificationDto) {
    List<SseEmitter> emitters = sseEmitterRepository.findByUserId(receiverId);

    if (emitters == null || emitters.isEmpty()) {
      log.debug("userId [{}]에 대한 활성된 SSE 연결이 없음.", receiverId);
      return;
    }

    for (SseEmitter emitter : emitters) {
      sendToClient(receiverId, emitter, "notifications", notificationDto);
    }
  }

  public void sendBinaryContent(BinaryContent binaryContent) {
    UUID userId = userRepository.findByProfileId(binaryContent.getId()).orElseThrow(() -> {
      log.error("profileId [{}]에 대한 사용자 정보를 찾을 수 없음", binaryContent.getId());
      return new UserNotFoundException();
    }).getId();

    sendBinaryContent(userId, binaryContent);
  }

  public void sendBinaryContent(UUID userId, BinaryContent binaryContent) {
    List<SseEmitter> emitters = sseEmitterRepository.findByUserId(userId);

    if (emitters == null || emitters.isEmpty()) {
      log.debug("userId [{}]에 대한 활성된 SSE 연결이 없음.", userId);
      return;
    }

    BinaryContentDto binaryContentDto = BinaryContentDto.from(binaryContent);
    for (SseEmitter emitter : emitters) {
      sendToClient(userId, emitter, "binaryContents.status", binaryContentDto);
    }
  }

  public void sendChannelRefresh(UUID userId, UUID channelId) {
    List<SseEmitter> emitters = sseEmitterRepository.findByUserId(userId);

    if (emitters == null || emitters.isEmpty()) {
      log.debug("userId [{}]에 대한 활성된 SSE 연결이 없음.", userId);
      return;
    }

    for (SseEmitter emitter : emitters) {
      sendToClient(userId, emitter, "channels.refresh",
          Map.of("channelId", channelId.toString()));
    }
  }

  public void sendPrivateChannelRefresh(List<UUID> userIds, UUID channelId) {
    userIds.forEach(userId -> {
      List<SseEmitter> emitters = sseEmitterRepository.findByUserId(userId);

      if (emitters == null || emitters.isEmpty()) {
        log.debug("userId [{}]에 대한 활성된 SSE 연결이 없음.", userId);
        return;
      }

      for (SseEmitter emitter : emitters) {
        sendToClient(userId, emitter, "channels.refresh",
            Map.of("channelId", channelId.toString()));
      }
    });
  }

  public void sendAllUsersRefresh(UUID changedUserId) {
    sseEmitterRepository.findAllEmitters().keySet().forEach(userId -> {
      List<SseEmitter> emitters = sseEmitterRepository.findByUserId(userId);

      if (emitters == null || emitters.isEmpty()) {
        log.debug("userId [{}]에 대한 활성된 SSE 연결이 없음.", userId);
        return;
      }

      for (SseEmitter emitter : emitters) {
        sendToClient(userId, emitter, "users.refresh", Map.of("userId", changedUserId.toString()));
      }
    });
  }

  @Scheduled(fixedRate = 120_000)
  public void pingAll() {
    sseEmitterRepository.findAllEmitters()
        .forEach((userId, list) -> list.forEach(emitter -> {
          try {
            emitter.send(SseEmitter.event().comment("ping"));
          } catch (IOException e) {
            log.debug("userId [{}]에 대한 활성된 SSE 연결이 끊어져 삭제 조치. -> emitter: {}", userId, emitter, e);
            sseEmitterRepository.delete(userId, emitter);
          }
        }));
  }

  @Scheduled(fixedRate = 3_600_000)
  public void cleanupUserEvents() {
    long oneHourAgo = System.currentTimeMillis() - (3600 * 1000);

    userEvents.forEach((userId, events) -> {
      events.removeIf(event -> {
        try {
          return Long.parseLong(event.getId()) < oneHourAgo;
        } catch (NumberFormatException e) {
          return false;
        }
      });

      if (events.isEmpty()) {
        userEvents.remove(userId);
      }
    });
    log.info("오래된 SSE 이벤트 삭제. 현재 userEvents의 크기: {}", userEvents.size());
  }

  private void sendToClient(UUID userId, SseEmitter emitter, String eventName, Object data) {
    String eventId = String.valueOf(eventIdCounter.incrementAndGet());
    SseEvent sseEvent = new SseEvent(eventId, eventName, data);

    userEvents.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(sseEvent);

    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name(eventName)
          .data(data));
      log.debug("[{}]의 {} SSE 이벤트 수신 완료 (ID: {})", userId, eventName, eventId);
    } catch (IOException e) {
      sseEmitterRepository.delete(userId, emitter);
      log.error("[{}]의 {} SSE 이벤트 실패 (ID: {})", userId, eventName, eventId, e);
    }
  }

  private void sendToClient(UUID userId, SseEmitter emitter, String eventId, String eventName,
      Object data) {
    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name(eventName)
          .data(data));
      log.debug("[{}]의 {} SSE 이벤트 재전송 완료 (ㄴID: {})", userId, eventName, eventId);
    } catch (IOException e) {
      sseEmitterRepository.delete(userId, emitter);
      log.error("[{}]의 {} SSE 이벤트 재전송 실패 (ID: {})", userId, eventName, eventId, e);
    }
  }
}
