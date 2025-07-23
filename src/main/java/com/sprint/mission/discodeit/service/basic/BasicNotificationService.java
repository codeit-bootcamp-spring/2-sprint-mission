package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.controller.api.NotificationApi;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Cacheable(value = "notifications", key = "#receiverId")
    public List<NotificationDto> getNotifications(UUID receiverId) {
            List<Notification> notifications = notificationRepository.findByReceiverId(receiverId);


        return notifications.stream()
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "notifications", key = "#notificationId")
    public void deleteNotification(UUID notificationId, UUID receiverId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);

        if(!notification.getReceiver().getId().equals(receiverId)) {
            throw new IllegalArgumentException("본인의 알람만 삭제할 수 있음");
        }

        notificationRepository.delete(notification);
    }


    @Override
    public void createNotificationChannel(UUID receiverId, UUID channelId) {
        eventPublisher.publishEvent(new NotificationEvent(
                receiverId,
                "새 메세지",
                "새로운 메세지가 도착하였습니다.",
                NotificationType.NEW_MESSAGE,
                channelId
        ));
    }

    @Override
    public void createNotificationRoleChanged(UUID receiverId, UUID userId) {
        eventPublisher.publishEvent(new NotificationEvent(
                receiverId,
                "역할 변경",
                "역할이 변경되었습니다.",
                NotificationType.ROLE_CHANGED,
                userId
        ));
    }

    @Override
    public void createNotificationAsyncFailed(UUID receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> UserNotFoundException.withId(receiverId));

        Notification notification = new Notification(receiver, "비동기 작업 실패", "비동기 작업이 실패하였습니다.", NotificationType.ASYNC_FAILED);
        notificationRepository.save(notification);
    }
}
