package com.sprint.mission.discodeit.adapter.inbound.channel.dto;

import java.util.UUID;

public record PublicChannelCreateCommand(
    UUID userId,
    String name
) {

}
