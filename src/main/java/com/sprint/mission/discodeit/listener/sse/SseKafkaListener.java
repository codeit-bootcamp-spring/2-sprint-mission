package com.sprint.mission.discodeit.listener.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.sse.SseBinaryContentStatusEvent;
import com.sprint.mission.discodeit.event.sse.SseChannelRefreshEvent;
import com.sprint.mission.discodeit.event.sse.SseNotificationEvent;
import com.sprint.mission.discodeit.event.sse.SseStoredEvent;
import com.sprint.mission.discodeit.event.sse.SseUserRefreshEvent;
import com.sprint.mission.discodeit.listener.sse.SseEventIdGenerator;
import com.sprint.mission.discodeit.repository.SseEventStore;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.SseService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseKafkaListener {

  private final SseService sseService;
  private final SseEventStore eventStore;
  private final SseEventIdGenerator idGenerator;
  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;
  private final CacheManager cacheManager;

  @KafkaListener(topics = "sse.notification", groupId = "sse-group")
  public void handleSseNotification(
      @Payload String message,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    try {
      SseNotificationEvent event = objectMapper.readValue(message, SseNotificationEvent.class);
      String eventId = idGenerator.generateEventId();

      log.debug("SSE 알림 이벤트 수신: receiverId={}, eventId={}",
          event.receiverId(), eventId);

      SseStoredEvent storedEvent = new SseStoredEvent(
          eventId,
          "notifications",
          event.timestamp(),
          objectMapper.writeValueAsString(event.notification())
      );
      eventStore.saveEvent(event.receiverId(), storedEvent);

      sseService.sendNotificationEvent(event.receiverId(), event.notification());

    } catch (Exception e) {
      log.error("SSE 알림 이벤트 처리 실패: topic={}", topic, e);
    }
  }

  @KafkaListener(topics = "sse.binary-content-status", groupId = "sse-group")
  public void handleSseBinaryContentStatus(
      @Payload String message,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    try {
      SseBinaryContentStatusEvent event = objectMapper.readValue(message,
          SseBinaryContentStatusEvent.class);
      String eventId = idGenerator.generateEventId();

      log.debug("SSE 파일 상태 이벤트 수신: receiverId={}, binaryContentId={}, eventId={}",
          event.receiverId(), event.binaryContent().id(), eventId);

      SseStoredEvent storedEvent = new SseStoredEvent(
          eventId,
          "binaryContents.status",
          event.timestamp(),
          objectMapper.writeValueAsString(event.binaryContent())
      );
      eventStore.saveEvent(event.receiverId(), storedEvent);

      sseService.sendBinaryContentStatusEvent(event.receiverId(), event.binaryContent());

    } catch (Exception e) {
      log.error("SSE 파일 상태 이벤트 처리 실패: topic={}", topic, e);
    }
  }

  @KafkaListener(topics = "sse.channel-refresh", groupId = "sse-group")
  public void handleSseChannelRefresh(
      @Payload String message,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    try {
      SseChannelRefreshEvent event = objectMapper.readValue(message, SseChannelRefreshEvent.class);
      String eventId = idGenerator.generateEventId();

      log.debug("SSE 채널 갱신 이벤트 수신: receiverId={}, channelId={}, eventId={}",
          event.receiverId(), event.channelId(), eventId);

      Map<String, String> data = Map.of("channelId", event.channelId().toString());
      SseStoredEvent storedEvent = new SseStoredEvent(
          eventId,
          "channels.refresh",
          event.timestamp(),
          objectMapper.writeValueAsString(data)
      );
      eventStore.saveEvent(event.receiverId(), storedEvent);

      sseService.sendChannelRefreshEvent(event.receiverId(), event.channelId());

    } catch (Exception e) {
      log.error("SSE 채널 갱신 이벤트 처리 실패: topic={}", topic, e);
    }
  }

  @KafkaListener(topics = "sse.user-refresh", groupId = "sse-group")
  public void handleSseUserRefresh(
      @Payload String message,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
    try {
      SseUserRefreshEvent event = objectMapper.readValue(message, SseUserRefreshEvent.class);
      String eventId = idGenerator.generateEventId();

      Objects.requireNonNull(cacheManager.getCache("users")).evict("all");

      if (event.receiverId() == null) {
        log.debug("SSE 사용자 갱신 이벤트 수신 (전체): targetUserId={}, eventId={}",
            event.targetUserId(), eventId);

        List<UUID> allUserIds = userRepository.findAll().stream()
            .map(User::getId)
            .toList();

        Map<String, String> data = Map.of("userId", event.targetUserId().toString());
        String jsonData = objectMapper.writeValueAsString(data);

        for (UUID userId : allUserIds) {
          SseStoredEvent storedEvent = new SseStoredEvent(
              eventId,
              "users.refresh",
              event.timestamp(),
              jsonData
          );
          eventStore.saveEvent(userId, storedEvent);

          sseService.sendUserRefreshEvent(userId, event.targetUserId());
        }

        log.info("SSE 사용자 갱신 이벤트 전체 전송 완료: targetUserId={}, 대상 수={}",
            event.targetUserId(), allUserIds.size());
      } else {
        log.debug("SSE 사용자 갱신 이벤트 수신: receiverId={}, targetUserId={}, eventId={}",
            event.receiverId(), event.targetUserId(), eventId);

        Map<String, String> data = Map.of("userId", event.targetUserId().toString());
        SseStoredEvent storedEvent = new SseStoredEvent(
            eventId,
            "users.refresh",
            event.timestamp(),
            objectMapper.writeValueAsString(data)
        );
        eventStore.saveEvent(event.receiverId(), storedEvent);

        sseService.sendUserRefreshEvent(event.receiverId(), event.targetUserId());
      }

    } catch (Exception e) {
      log.error("SSE 사용자 갱신 이벤트 처리 실패: topic={}", topic, e);
    }
  }
}