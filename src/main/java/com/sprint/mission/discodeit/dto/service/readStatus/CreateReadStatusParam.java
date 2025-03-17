package com.sprint.mission.discodeit.dto.service.readStatus;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateReadStatusParam(
        UUID userId,
        UUID channelId
){
}
