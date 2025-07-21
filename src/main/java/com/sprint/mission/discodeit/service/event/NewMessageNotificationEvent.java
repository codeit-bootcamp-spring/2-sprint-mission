package com.sprint.mission.discodeit.service.event;

import com.sprint.mission.discodeit.entity.Message;

public record NewMessageNotificationEvent(Message message) {
}
