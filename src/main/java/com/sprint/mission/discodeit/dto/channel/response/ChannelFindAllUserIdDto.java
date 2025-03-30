package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.groups.ChannelType;
import jakarta.validation.constraints.NotNull;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindAllUserIdDto(
        @NotNull
        UUID channelId,
        @NotNull
        String name,
        @NotNull
        String description,

        @NotNull
        Instant updatedAt,

        List<UUID> userIds,

        @NotNull
        ChannelType channelType
){
        public static ChannelFindAllUserIdDto from(Channel channel) {
                return new ChannelFindAllUserIdDto(
                        channel.getId(),
                        channel.getName(),
                        channel.getDescription(),
                        channel.getUpdatedAt(),
                        channel.getUserIds(),
                        channel.getChannelType()
                );
        }
}
