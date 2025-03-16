package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.UUID;

public record ReadStatusCreateDTO(
        String userId,
        String channelID
) {
}
