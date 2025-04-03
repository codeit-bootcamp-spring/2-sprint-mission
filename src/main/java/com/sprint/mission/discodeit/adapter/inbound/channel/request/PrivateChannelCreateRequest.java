package com.sprint.mission.discodeit.adapter.inbound.channel.request;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
    List<UUID> participantIds

) {

}
