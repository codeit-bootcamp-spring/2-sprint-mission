package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicNotificationService implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;

  // 본인 notifications return
  @Override
  public List<NotificationDto> findByRecevierId(UUID receiverId) {
    return notificationRepository.findByReceiverId(receiverId)
        .stream()
        .map(notificationMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(UUID notificationId, UUID receiverId) throws AccessDeniedException {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new RuntimeException("알림이 존재하지 않습니다."));
    if (!Objects.equals(notification.getReceiverId(), receiverId)) {
      throw new AccessDeniedException("자신의 알림만 삭제할 수 있습니다.");
    }
    notificationRepository.deleteById(notificationId);
  }

  // 알림 저장
  public void save(Notification notification) {
    notificationRepository.save(notification);
  }
}
