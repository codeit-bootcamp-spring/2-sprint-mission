package com.sprint.mission.discodeit.application.channel;

import com.sprint.mission.discodeit.application.user.UsersDto;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record ChannelResponseDto(UUID id, String name, ChannelType type, Instant lastMessageCreatedAt,
                                 UsersDto usersDto) {
    public static ChannelResponseDto fromEntity(ChannelDto channel, Instant lastMessageCreatedAt, UsersDto usersDto) {
        return new ChannelResponseDto(channel.id(), channel.name(), channel.type(), lastMessageCreatedAt, usersDto);
    }
}