package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class NotificationEvent {
    private final UUID receiverId;
    private final String title;
    private final String content;
    private final NotificationType type;
    private final UUID targetId;
}
