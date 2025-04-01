package com.sprint.mission.discodeit.adapter.inbound.channel.dto;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequestDTO(
        List<UUID> participantIds
) {
}
