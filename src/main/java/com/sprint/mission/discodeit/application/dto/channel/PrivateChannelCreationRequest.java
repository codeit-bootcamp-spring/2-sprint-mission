package com.sprint.mission.discodeit.application.dto.channel;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreationRequest(@NotNull String channelName, @NotNull UUID creatorId,
                                            List<UUID> memberIds) {
}
