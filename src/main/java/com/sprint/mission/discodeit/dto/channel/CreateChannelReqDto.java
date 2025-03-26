package com.sprint.mission.discodeit.dto.channel;


import java.util.List;
import java.util.UUID;

public record CreateChannelReqDto() {
    public record Public(
            String name,
            String description
    ) {

    }

    public record Private(
            List<UUID> participantUserIds
    ) {

    }
}
