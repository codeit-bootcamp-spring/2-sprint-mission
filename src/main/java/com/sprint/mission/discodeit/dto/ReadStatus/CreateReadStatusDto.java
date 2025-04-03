package com.sprint.mission.discodeit.dto.ReadStatus;

import java.util.UUID;

public record CreateReadStatusDto(
        UUID userId,
        UUID channelId
) {}

