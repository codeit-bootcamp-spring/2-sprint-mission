package com.sprint.mission.discodeit.common.event.event;

import com.sprint.mission.discodeit.domain.channel.entity.Channel;
import com.sprint.mission.discodeit.domain.message.entity.Message;
import com.sprint.mission.discodeit.domain.notification.entity.NotificationType;
import java.util.UUID;

public class NewMessageNotificationEvent extends NotificationEvent {

  public NewMessageNotificationEvent(Message message) {
    super(
        message.getUser().getId(),
        NotificationType.NEW_MESSAGE,
        message.getChannel().getId(),
        NotificationType.NEW_MESSAGE.getTitle(),
        createContent(message)
    );
  }

  private static String createContent(Message message) {
    Channel messageChannel = message.getChannel();
    String contentKeyword = messageChannel.getType().name();
    if (messageChannel.isPublic()) {
      contentKeyword = messageChannel.getName();
    }

    return NotificationType.NEW_MESSAGE.formatContent(contentKeyword);
  }

}
