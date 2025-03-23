package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ChannelDTO(
        UUID id,
        String channelName,
        boolean isPublic,
        Instant createAt,
        Instant updateAt,
        Set<UUID> participantIds
) {
    public static ChannelDTO fromName(String channelname) {
        return new ChannelDTO(null, channelname, true, null, null, null);
    }

    public static ChannelDTO fromEntity(Channel channel) {
        return new ChannelDTO(
                channel.getId(),
                channel.getChannelName(),
                channel.isPublic(),
                channel.getCreateAt(),
                channel.getUpdateAt(),
                channel.getParticipants() != null ?
                        channel.getParticipants().stream().map(User::getId).collect(Collectors.toSet()) :
                        Set.of()
        );
    }
}
