package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.notification.NotificationCreateAllDto;
import com.sprint.mission.discodeit.dto.notification.NotificationCreateDto;
import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void createNotification(NotificationCreateDto notificationCreateDto) {
        Notification notification = new Notification(
            notificationCreateDto.receiverId(),
            notificationCreateDto.title(),
            notificationCreateDto.content(),
            notificationCreateDto.type(),
            notificationCreateDto.targetId()
        );
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void createAllNotifications(NotificationCreateAllDto notificationCreateAllDto) {
        log.info("메세지 알람 서비스 시작: channelId = {}", notificationCreateAllDto.channelId());
        notificationCreateAllDto.receiverIds().forEach(receiverId -> {
            Notification notification = new Notification(
                receiverId,
                notificationCreateAllDto.title(),
                notificationCreateAllDto.content(),
                notificationCreateAllDto.type(),
                notificationCreateAllDto.channelId()
            );
            notificationRepository.save(notification);
        });
        log.info("메세지 알람 서비스 종료: channelId = {}", notificationCreateAllDto.channelId());
    }

    @Override
    public NotificationDto readNotification(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> NotificationNotFoundException.forId(notificationId.toString()));
        return notificationMapper.toDto(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> readNotifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) auth.getPrincipal();
        UUID userId = userDetails.getUserId();

        List<Notification> notifications = notificationRepository.findByReceiverId(userId);

        return notifications.stream()
            .map(notificationMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public void deleteNotification(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
