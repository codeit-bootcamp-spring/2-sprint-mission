package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicNotificationCommandService implements NotificationCommandService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Override
  public void create(NotificationEvent event) {
    User receiver = userRepository.findById(event.receiverId())
        .orElseThrow(() -> UserNotFoundException.withId(event.receiverId()));

    Notification notification = new Notification(
        receiver,
        event.type(),
        event.targetId()
    );

    notificationRepository.save(notification);
  }
}
