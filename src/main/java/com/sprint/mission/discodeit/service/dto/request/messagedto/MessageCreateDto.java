package com.sprint.mission.discodeit.service.dto.request.messagedto;

import java.util.UUID;

public record MessageCreateDto(
        String content,
        UUID channelId,
        UUID authorId

) {

}
