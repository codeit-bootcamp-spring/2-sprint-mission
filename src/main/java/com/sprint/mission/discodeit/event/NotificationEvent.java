package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationEvent {

  private final UUID receiverId;
  private final NotificationType type;
  private final String title;
  private final String content;
  private final UUID targetId;
}
