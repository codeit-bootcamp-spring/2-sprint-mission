package com.sprint.mission.discodeit.service.dto.channel;

public record ChannelUpdateRequest(
        String newName,
        String newDescription
) {
}
