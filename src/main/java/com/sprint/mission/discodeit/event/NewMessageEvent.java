package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.MessageDto;

public record NewMessageEvent(
    MessageDto messageDto
) {

}
