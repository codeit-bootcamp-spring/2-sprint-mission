package com.sprint.mission.discodeit.dto.service.readStatus;

import java.util.UUID;

public record CreateReadStatusParam(
        UUID userId,
        UUID channelId
){
}
