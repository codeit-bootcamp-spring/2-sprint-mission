package com.sprint.mission.discodeit.dto.controller.message;

import com.sprint.mission.discodeit.dto.service.message.MessageDTO;

import java.util.List;

public record MessageListDTO(
        List<MessageDTO> messageDTOList
) {
}
