package com.sprint.mission.discodeit.service.dto.channeldto;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.dto.userdto.UserResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        ChannelType type,
        String name,
        String description,
        List<UserResponseDto> participants,
        Instant lastMessageAt


) {
}