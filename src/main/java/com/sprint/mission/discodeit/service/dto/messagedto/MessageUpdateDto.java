package com.sprint.mission.discodeit.service.dto.messagedto;

import java.util.UUID;

public record MessageUpdateDto(
        UUID messageId,
        String changeMessage

) {

}
