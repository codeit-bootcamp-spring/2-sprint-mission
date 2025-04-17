package com.sprint.mission.discodeit.dto.service.channel;

public record UpdateChannelCommand(
    String newName,
    String newDescription
) {

}
