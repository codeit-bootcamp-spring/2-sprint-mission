package com.sprint.mission.discodeit.dto.controller.message;


import com.sprint.mission.discodeit.dto.service.message.FindMessageResult;
import java.util.List;

public record MessageListDTO(
    List<FindMessageResult> messageDTOList
) {

}
