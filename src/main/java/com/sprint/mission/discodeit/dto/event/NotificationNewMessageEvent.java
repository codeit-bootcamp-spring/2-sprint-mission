package com.sprint.mission.discodeit.dto.event;

import com.sprint.mission.discodeit.dto.message.MessageDto;

public record NotificationNewMessageEvent(
    MessageDto messageDto
) {

}
