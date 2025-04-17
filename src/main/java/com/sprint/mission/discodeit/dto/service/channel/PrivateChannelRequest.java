package com.sprint.mission.discodeit.dto.service.channel;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record PrivateChannelRequest(
    @NotNull List<UUID> participantIds
) {

}
