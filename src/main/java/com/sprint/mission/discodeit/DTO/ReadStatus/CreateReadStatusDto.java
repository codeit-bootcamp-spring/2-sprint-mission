package com.sprint.mission.discodeit.DTO.ReadStatus;

import lombok.Getter;

import java.util.UUID;

public record CreateReadStatusDto(
        UUID userId,
        UUID channelId
) {}

