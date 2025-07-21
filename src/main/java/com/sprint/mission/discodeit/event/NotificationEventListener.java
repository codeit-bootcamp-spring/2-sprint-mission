package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final ReadStatusRepository readStatusRepository;
  private final ChannelService channelService;
  private final NotificationService notificationService;

  @Async
  @Retryable(
      retryFor = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  @KafkaListener(topics = "new-message")
  public void handleCreateMessageEvent(NewMessageEvent event) {
    try {
      MessageDto messageDto = event.messageDto();
      ChannelDto channel = channelService.find(messageDto.channelId());

      log.debug("메시지 생성 알림 이벤트 시작 - channelId : {}, messageId : {}", channel.id(), messageDto.id());

      Set<UUID> receivers = readStatusRepository.findAllByChannelIdAndNotificationEnabledTrue(
              channel.id())
          .stream()
          .map(ReadStatus::getUser).map(User::getId)
          .filter(id -> !id.equals(messageDto.author().id()))
          .collect(Collectors.toSet());

      String author = messageDto.author().username();
      String title = channel.type().equals(ChannelType.PUBLIC)
          ? String.format("[공개] %s - %s", channel.name(), author)
          : String.format("[비공개] %s", author);
      String content = messageDto.content();

      notificationService.createAll(receivers, title, content, NotificationType.NEW_MESSAGE,
          channel.id());
    } catch (Exception e) {
      log.error("메시지 생성 알림 이벤트 처리 중 오류 발생 :", e);
    }
  }

  @Async
  @Retryable(
      retryFor = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  @KafkaListener(topics = "role-changed")
  public void handleUpdateRoleEvent(RoleChangedEvent event) {
    try {
      UserDto userDto = event.userDto();
      UUID receiver = userDto.id();

      log.debug("사용자 권한 변경 알림 이벤트 시작 - userId : {}, Role : {}", userDto.id(), userDto.role());

      String title = String.format("[권한 변경] %s", userDto.username());
      String content = String.format("%s -> %s", event.previousRole(), userDto.role());

      notificationService.create(receiver, title, content, NotificationType.ROLE_CHANGED, receiver);
    } catch (Exception e) {
      log.error("사용자 권한 변경 알림 이벤트 처리 중 오류 발생 :", e);
    }
  }

  @Async
  @Retryable(
      retryFor = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  @KafkaListener(topics = "async-failure")
  public void handleAsyncFailureEvent(AsyncFailureEvent event) {
    try {
      AsyncTaskFailure asyncTaskFailure = event.failure();
      DiscodeitUserDetails userDetails = (DiscodeitUserDetails) SecurityContextHolder.getContext()
          .getAuthentication().getDetails();
      UserDto userDto = userDetails.getUserDto();

      log.debug("비동기 작업 실패 알림 이벤트 시작 - taskName : {}, userId : {}", asyncTaskFailure.getTaskName(),
          userDto.id());

      String title = String.format("[비동기 작업 실패] %s", asyncTaskFailure.getTaskName());
      String content = String.format("failure reason: %s", asyncTaskFailure.getFailureReason());

      notificationService.create(userDto.id(), title, content, NotificationType.ASYNC_FAILED, null);
    } catch (Exception e) {
      log.error("비동기 작업 실패 알림 이벤트 처리 중 오류 발생 :", e);
    }
  }
}
