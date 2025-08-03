package com.sprint.mission.discodeit.listener;

import com.sprint.mission.discodeit.common.NotificationEvent;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.event.MessageCreatedEvent;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final ReadStatusRepository readStatusRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @EventListener
    public void handleMessageCreated(MessageCreatedEvent event) {
        Message message = event.getMessage();
        User sender = message.getAuthor();
        Channel channel = message.getChannel();

        List<ReadStatus> notificationTargets = readStatusRepository
                .findAllByChannelIdAndNotificationEnabled(channel.getId(), true);

        notificationTargets.stream()
                .filter(readStatus -> !readStatus.getUser().getId().equals(sender.getId()))
                .forEach(readStatus -> {
                    String notificationContent = String.format("`%s` 채널에 새로운 메시지가 도착했습니다.", channel.getName());
                    NotificationEvent notificationEvent = new NotificationEvent(
                            readStatus.getUser().getId(),
                            NotificationType.NEW_MESSAGE,
                            channel.getId(),
                            notificationContent
                    );
                    eventPublisher.publishEvent(notificationEvent);
                });
    }
} 