package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelResponseDto(
        UUID id,
        ChannelType type,
        String name,
        String description,
        List<UserResponseDto> participants,
        Instant lastMessageAt


) {
}