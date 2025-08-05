package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.EmitterRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.SseService;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicSseService implements SseService {

  private static final long SSE_TIMEOUT = 60 * 60 * 1000L;

  private final EmitterRepository emitterRepository;
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final Map<UUID, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

  @Override
  public SseEmitter subscribe(UUID userId, String lastEventId) {
    SseEmitter emitter = emitterRepository.save(userId, new SseEmitter(SSE_TIMEOUT));

    if (lastEventId != null && !lastEventId.isEmpty()) {
      restoreMissEvents(userId, lastEventId, emitter);
    }
    return emitter;
  }

  @Transactional
  @Override
  public void sendNotification(NotificationDto notificationDto) {
    Notification notification = notificationRepository.save(
        new Notification(notificationDto.receiverId(), notificationDto.title(), notificationDto.content(), notificationDto.type())
    );

    String eventId = notification.getCreatedAt() + "_" + notification.getId().toString();
    UUID receiverId = notification.getReceiverId();

    emitterRepository.get(receiverId)
        .ifPresent(emitters -> emitters.forEach(
            emitter -> sendToEmitter(emitter, eventId, notificationMapper.toDto(notification), "notification"))
        );
  }

  @Transactional
  @Override
  public void sendBinaryContentStatus(UUID uploaderId, BinaryContentDto binaryContentDto) {
    String eventName = "binaryContents.status";
    String eventId = Instant.now().toString() + "_" + binaryContentDto.id();

    emitterRepository.get(uploaderId)
        .ifPresent(emitters -> emitters.forEach(
            emitter -> sendToEmitter(emitter, eventId, binaryContentDto, eventName))
        );
  }

  @Scheduled(fixedRate = 30000)
  public void sendPing() {
    Map<UUID, CopyOnWriteArrayList<SseEmitter>> allEmitters = emitterRepository.getAllEmitters();
    allEmitters.forEach((userId, emitters) -> {
      emitters.forEach(emitter -> {
        try {
          emitter.send(SseEmitter.event().comment("ping"));
        } catch (IOException e) {
          log.warn("Heartbeat failed for userId={}, removing emitter.", userId);
          emitter.completeWithError(e);
        }
      });
    });
  }

  @Override
  public void broadcast(String eventName, Object data) {
    String eventId = "broadcast_" + eventName + "_" + System.currentTimeMillis();
    log.info("SSE 브로드캐스트 시작: eventName={}", eventName);

    emitters.values().stream()
        .flatMap(List::stream)
        .forEach(emitter -> sendToEmitter(emitter, eventId, data, eventName));

    log.info("SSE 브로드캐스트 완료: eventName={}", eventName);
  }

  private void restoreMissEvents(UUID userId, String lastEventId, SseEmitter emitter) {
    String[] parts = lastEventId.split("_", 2);
    if (parts.length != 2) {
      log.warn("Invalid Last-Event-ID format: {}", lastEventId);
      return;
    }

    try {
      Instant createdAt = Instant.parse(parts[0]);
      List<Notification> missedEvents = notificationRepository.findByReceiverIdAndCreatedAtAfterOrderByCreatedAtAsc(userId, createdAt);

      missedEvents.forEach(notification -> {
        String eventId = notification.getCreatedAt() + "_" + notification.getId().toString();
        sendToEmitter(emitter, eventId, notificationMapper.toDto(notification), "notifications");
      });
    } catch (DateTimeParseException e) {
      log.warn("Failed to parse timestamp from Last-Event-ID: {}", lastEventId, e);
    }
  }



  private void sendToEmitter(SseEmitter emitter, String eventId, Object data, String eventName) {
    try {
      emitter.send(SseEmitter.event()
          .id(eventId)
          .name(eventName)
          .data(data));
    } catch (IOException e) {
      log.warn("emitter 전송 실패: {}", e.getMessage());
      emitter.completeWithError(e);
    }
  }
}
