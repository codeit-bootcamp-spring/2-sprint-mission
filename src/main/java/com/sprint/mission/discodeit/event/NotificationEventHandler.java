package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.constant.NotificationType;
import com.sprint.mission.discodeit.constant.Role;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.event.NotificationAsyncFailedEvent;
import com.sprint.mission.discodeit.dto.event.NotificationNewMessageEvent;
import com.sprint.mission.discodeit.dto.event.NotificationRoleUpdateEvent;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.notification.NotificationCreateAllDto;
import com.sprint.mission.discodeit.dto.notification.NotificationCreateDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationService notificationService;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelService channelService;
    private final ObjectMapper objectMapper;

    @Async("notificationExecutor")
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    @KafkaListener(topics = "discodeit.new-message")
    public void handleNewMessage(String kafkaMessage) throws JsonProcessingException {
        NotificationNewMessageEvent event = objectMapper.readValue(kafkaMessage, NotificationNewMessageEvent.class);
        try {
            log.info("메세지 알람 비동기 처리 시작: messageId = {}", event.messageDto().id());
            MessageDto messageDto = event.messageDto();
            UserDto userDto = messageDto.author();
            ChannelDto channelDto = channelService.findById(event.messageDto().channelId());
            String channelName = channelDto.name() == null ? "비공개 채널" : channelDto.name();

            Set<UUID> receiverIds = readStatusRepository
                .findAllByChannelIdAndNotificationEnabledTrue(messageDto.channelId())
                .stream()
                .map(ReadStatus::getUser)
                .map(User::getId)
                .filter(id -> !id.equals(messageDto.author().id()))
                .collect(Collectors.toSet());

            String title = String.format("[채널-%s][%s]", channelName, userDto.username());
            String content = messageDto.content();
            NotificationType type = NotificationType.NEW_MESSAGE;
            UUID channelId = messageDto.channelId();

            NotificationCreateAllDto notificationCreateAllDto = new NotificationCreateAllDto(
                receiverIds,
                title,
                content,
                type,
                channelId
            );

            notificationService.createAllNotifications(notificationCreateAllDto);
            log.info("메세지 알람 비동기 처리 완료: messageId = {}", event.messageDto().id());
        } catch (Exception e) {
            log.error("메세지 알람 비동기 처리 실패: error = {}", e.getMessage());
        }
    }

    @Async("notificationExecutor")
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    @KafkaListener(topics = "discodeit.role-changed")
    public void handleUpdateRole(String kafkaMessage) throws JsonProcessingException {
        NotificationRoleUpdateEvent event = objectMapper.readValue(kafkaMessage, NotificationRoleUpdateEvent.class);
        try {
            log.info("사용자 권한 변경 알람 비동기 처리 시작: userId = {}", event.userDto().id());
            UserDto userDto = event.userDto();

            UUID receiverId = userDto.id();
            Role previousRole = event.previousRole();
            String title = String.format("권한 변경: %s", userDto.role().name());
            String content = String.format("%s 권한에서 %s 권한으로 변경", previousRole.name(), userDto.role().name());
            NotificationType type = NotificationType.ROLE_CHANGED;
            UUID targetId = userDto.id();

            NotificationCreateDto notificationCreateDto = new NotificationCreateDto(
                receiverId,
                title,
                content,
                type,
                targetId
            );

            notificationService.createNotification(notificationCreateDto);
            log.info("사용자 권한 변경 알람 비동기 처리 완료: userId = {}", event.userDto().id());
        } catch (Exception e) {
            log.error("사용자 권한 변경 알람 비동기 처리 실패: error = {}", e.getMessage());
        }
    }

    @Async("notificationExecutor")
    @Retryable(
        value = { Exception.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    @KafkaListener(topics = "discodeit.async-task-failed")
    public void handleAsyncFailed(String kafkaMessage) throws JsonProcessingException {
        NotificationAsyncFailedEvent event = objectMapper.readValue(kafkaMessage, NotificationAsyncFailedEvent.class);
        try {
            log.info("비동기 작업 실패 알람 처리 시작: requestId = {}, taskName = {}, failureReason = {}",
                event.requestId(), event.taskName(), event.failureReason());

            UUID requestId = event.requestId();
            String title = String.format("비동기 작업 실패: requestId = %s", requestId);
            String content = String.format("요청 ID: %s\n작업 이름: %s\n실패 사유: %s",
                event.requestId(), event.taskName(), event.failureReason());
            NotificationType type = NotificationType.ASYNC_FAILED;

            NotificationCreateDto notificationCreateDto = new NotificationCreateDto(
                requestId,
                title,
                content,
                type,
                null
            );

            notificationService.createNotification(notificationCreateDto);
            log.info("비동기 작업 실패 알람 처리 완료: requestId = {}, taskName = {}, failureReason = {}",
                event.requestId(), event.taskName(), event.failureReason());
        } catch (Exception e) {
            log.info("비동기 작업 실패 알람 처리 실패: error = {}", e.getMessage());
        }
    }
}
