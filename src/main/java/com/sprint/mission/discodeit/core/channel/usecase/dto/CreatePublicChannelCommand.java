package com.sprint.mission.discodeit.core.channel.usecase.dto;

import java.util.UUID;

public record CreatePublicChannelCommand(
    UUID userId,
    String name
) {

}
