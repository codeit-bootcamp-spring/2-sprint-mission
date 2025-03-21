package com.sprint.mission.discodeit.dto.requestToService;

public record MessageWriteDTO(
        String creatorId,
        String channelId,
        String text
) {
}
