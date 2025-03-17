package com.sprint.mission.discodeit.DTO.Server;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ServerDTO(
        UUID serverId,
        UUID userId,
        String name,
        Instant createdAt,
        Instant updatedAt,
        List<User> userList
) {
    public static ServerDTO join(
            UUID serverId,
            UUID userId
    ) {
        return ServerDTO.builder()
                .serverId(serverId)
                .userId(userId).build();
    }

    public static ServerDTO create(
            UUID ownerId,
            String name) {
        return ServerDTO.builder()
                .userId(ownerId)
                .name(name).build();
    }

    public static ServerDTO delete(
            UUID serverId,
            UUID ownerId
    ) {
        return ServerDTO.builder()
                .serverId(serverId)
                .userId(ownerId).build();
    }

    public static ServerDTO update(
            UUID replaceServerId,
            UUID replaceOwnerId,
            String replaceName
    ) {
        return ServerDTO.builder()
                .serverId(replaceServerId)
                .userId(replaceOwnerId)
                .name(replaceName).build();
    }

}
