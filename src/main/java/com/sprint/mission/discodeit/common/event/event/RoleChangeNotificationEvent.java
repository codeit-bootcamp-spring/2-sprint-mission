package com.sprint.mission.discodeit.common.event.event;

import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import com.sprint.mission.discodeit.domain.user.entity.User;

public class RoleChangeNotificationEvent extends NotificationEvent {

  public RoleChangeNotificationEvent(User notUpdatedUser, User updatedUser) {
    super(
        updatedUser.getId(),
        NotificationType.ROLE_CHANGED,
        updatedUser.getId(),
        NotificationType.ROLE_CHANGED.getTitle(),
        createContent(notUpdatedUser, updatedUser)
    );
  }

  private static String createContent(User notUpdatedUser, User updatedUser) {
    String rawContent = String.format("%s에서 %s", notUpdatedUser.getRole().name(),
        updatedUser.getRole().name());
    return NotificationType.ROLE_CHANGED.formatContent(rawContent);
  }

}
