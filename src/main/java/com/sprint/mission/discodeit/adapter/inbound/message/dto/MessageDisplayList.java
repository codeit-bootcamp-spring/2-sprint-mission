package com.sprint.mission.discodeit.adapter.inbound.message.dto;

import java.util.List;

public record MessageDisplayList(
    List<MessageFindDTO> messages
) {

}
