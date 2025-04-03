package com.sprint.mission.discodeit.core.channel.usecase.dto;

public record CreatePublicChannelCommand(
    String name,
    String description
) {

}
