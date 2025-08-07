package com.sprint.mission.discodeit.sse.listener;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.binarycontent.event.BinaryContentUploadStatusChangedEvent;
import com.sprint.mission.discodeit.domain.channel.event.ChannelCreatedEvent;
import com.sprint.mission.discodeit.domain.channel.event.ChannelUpdatedEvent;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.message.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.notification.event.NotificationCreatedEvent;
import com.sprint.mission.discodeit.domain.user.entity.User;
import com.sprint.mission.discodeit.domain.user.event.UserCreatedEvent;
import com.sprint.mission.discodeit.domain.user.event.UserDeletedEvent;
import com.sprint.mission.discodeit.domain.user.event.UserUpdatedEvent;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.sse.service.SseService;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SseEventHandler {

  private final SseService sseService;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;

  @EventListener
  public void onNotification(NotificationCreatedEvent notificationCreatedEvent) {
    sseService.broadcast(
        notificationCreatedEvent.notificationResult().receiverId(),
        "notifications",
        notificationCreatedEvent.notificationResult()
    );
  }

  @EventListener
  public void onBinaryContentStatusChanged(BinaryContentUploadStatusChangedEvent event) {
    BinaryContentResult binaryContentResult = event.binaryContentResult();

    UUID userId = findUserHasBinaryContents(binaryContentResult);
    sseService.broadcast(
        userId,
        "binaryContents.status",
        binaryContentResult
    );
  }

  private UUID findUserHasBinaryContents(BinaryContentResult binaryContentResult) {
    Optional<User> user = userRepository.findByBinaryContentId(
        binaryContentResult.id());
    Optional<Message> message = messageRepository.findByAttachmentId(
        binaryContentResult.id());

    if (user.isPresent()) {
      User foundUser = user.get();
      return foundUser.getId();
    }
    Message foundMessage = message.get();
    return foundMessage.getUser().getId();
  }

  @TransactionalEventListener
  public void onChannelCreated(ChannelCreatedEvent event) {
    UUID channelId = event.channelId();
    Map<String, Object> data = Map.of("channelId", channelId);
    sseService.broadcastALl("channels.refresh", data);
  }

  @TransactionalEventListener
  public void onChannelDeleted(ChannelUpdatedEvent event) {
    UUID channelId = event.channelId();
    Map<String, Object> data = Map.of("channelId", channelId);
    sseService.broadcastALl("channels.refresh", data);
  }

  @TransactionalEventListener
  public void onUserCreated(UserCreatedEvent event) {
    UUID userId = event.userId();
    Map<String, Object> data = Map.of("userId", userId);
    sseService.broadcast(userId, "users.refresh", data);
  }

  @TransactionalEventListener
  public void onUserUpdated(UserUpdatedEvent event) {
    UUID userId = event.userId();
    Map<String, Object> data = Map.of("userId", userId);
    sseService.broadcast(userId, "users.refresh", data);
  }

  @TransactionalEventListener
  public void onUserDeleted(UserDeletedEvent event) {
    Map<String, Object> data = Map.of("userId", "all");
    sseService.broadcastALl("users.refresh", data);
  }

}
