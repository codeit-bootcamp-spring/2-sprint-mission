package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.groups.ChannelType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelPrivateCreateDto(
    @NotNull
    UUID channelId,
    @NotNull
    UUID userId,
    @NotNull
    ChannelType channelType

) {
}
