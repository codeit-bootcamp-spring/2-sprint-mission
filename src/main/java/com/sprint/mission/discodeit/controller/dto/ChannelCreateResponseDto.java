package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.dto.ChannelResponseDto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record ChannelCreateResponseDto(
        ChannelType type,
        String category,
        String name
) {
    public static ChannelCreateResponseDto convertToResponseDto(Channel channel) {
        return new ChannelCreateResponseDto(channel.getType(), channel.getCategory(), channel.getName());
    }
}
