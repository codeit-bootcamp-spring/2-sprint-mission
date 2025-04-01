package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.UserRole;

import java.util.Set;
import java.util.UUID;

public record ChannelCreateDto(
        ChannelType type,
        String category,
        String name,
        UUID userId,
        Set<UUID> userMembers,
        UserRole writePermission
) {
    public Channel convertDtoToChannel() {
        return new Channel(type, category, name, userId, userMembers, writePermission);
    }
}
