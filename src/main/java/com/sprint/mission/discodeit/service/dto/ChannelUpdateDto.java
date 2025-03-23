package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.UserRole;

import java.util.Set;
import java.util.UUID;

public record ChannelUpdateDto(
        UUID id,
        ChannelType type,
        String category,
        String name,
        UUID userId,
        UserRole writePermission
) {
}
