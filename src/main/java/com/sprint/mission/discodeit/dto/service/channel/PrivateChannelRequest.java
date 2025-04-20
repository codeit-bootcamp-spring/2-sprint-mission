package com.sprint.mission.discodeit.dto.service.channel;

import java.util.List;
import java.util.UUID;

public record PrivateChannelRequest(
    List<UUID> participantIds
) {

}
