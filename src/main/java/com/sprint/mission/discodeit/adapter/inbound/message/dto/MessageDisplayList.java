package com.sprint.mission.discodeit.adapter.inbound.message.dto;

import com.sprint.mission.discodeit.core.message.usecase.crud.dto.MessageResult;
import java.util.List;

public record MessageDisplayList(
    List<MessageResult> messages
) {

}
