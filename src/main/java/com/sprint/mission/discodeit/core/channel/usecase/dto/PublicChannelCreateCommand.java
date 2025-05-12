package com.sprint.mission.discodeit.core.channel.usecase.dto;

public record PublicChannelCreateCommand(
    String name,
    String description
) {

}
