package com.sprint.mission.discodeit.DTO.RequestToService;

public record MessageWriteDTO(
        String creatorId,
        String channelId,
        String text
) {
}
