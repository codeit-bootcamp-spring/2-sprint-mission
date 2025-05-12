package com.sprint.mission.discodeit.core.channel.usecase.dto;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateCommand(
    List<UUID> participantIds
) {

}
