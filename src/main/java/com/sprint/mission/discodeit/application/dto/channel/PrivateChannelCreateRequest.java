package com.sprint.mission.discodeit.application.dto.channel;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(List<UUID> participantIds) {
}
