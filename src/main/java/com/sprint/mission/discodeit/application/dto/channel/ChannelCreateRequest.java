package com.sprint.mission.discodeit.application.dto.channel;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelCreateRequest(@NotNull String channelName, @NotNull UUID creatorId) {
}
