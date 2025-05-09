package com.sprint.mission.discodeit.dto.service.channel;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record PrivateChannelRequest(
    @NotEmpty List<UUID> participantIds
) {

}
