package com.sprint.mission.discodeit.core.message.usecase.crud.dto;

import java.util.List;

public record MessageListResult(
    List<MessageResult> messageList
) {

}
