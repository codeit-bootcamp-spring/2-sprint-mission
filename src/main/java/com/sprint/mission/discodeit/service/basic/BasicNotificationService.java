package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  @Override
  public List<NotificationDto> getNotifications(UserDto userDto) {
     return notificationRepository.findAllByReceiver_IdOrderByCreatedAtDesc(userDto.id())
         .stream()
         .map(notificationMapper::toDto)
         .toList();
  }

  @Override
  public void deleteNotification(UUID notificationId) {
    notificationRepository.deleteById(notificationId);
  }
}
