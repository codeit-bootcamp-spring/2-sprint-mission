package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelCreateRequest(@NotNull ChannelType channelType, @NotNull String name, @NotNull UUID logInUserId) {
}
