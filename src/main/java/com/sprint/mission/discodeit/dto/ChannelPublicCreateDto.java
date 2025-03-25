package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.groups.ChannelType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelPublicCreateDto(
    @NotNull
    UUID channelId,
    @NotNull
    String name,
    @NotNull
    String description,
    @NotNull
    UUID userId,
    @NotNull
    ChannelType channelType
) {
}
