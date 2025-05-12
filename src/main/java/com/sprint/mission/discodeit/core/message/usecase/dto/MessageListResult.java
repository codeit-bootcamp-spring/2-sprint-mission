package com.sprint.mission.discodeit.core.message.usecase.dto;

import java.util.List;

public record MessageListResult(
    List<MessageDto> messageList
) {

}
