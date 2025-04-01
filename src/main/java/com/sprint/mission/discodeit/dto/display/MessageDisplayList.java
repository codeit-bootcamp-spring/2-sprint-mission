package com.sprint.mission.discodeit.dto.display;

import com.sprint.mission.discodeit.dto.MessageFindDTO;

import java.util.List;

public record MessageDisplayList(
    List<MessageFindDTO> messages
) {
}
