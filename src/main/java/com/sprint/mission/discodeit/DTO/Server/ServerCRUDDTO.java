package com.sprint.mission.discodeit.DTO.Server;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ServerCRUDDTO(
        UUID serverId,
        UUID userId,
        String name
) {
    public static ServerCRUDDTO join(UUID serverId,
                                     UUID userId) {
        return ServerCRUDDTO.builder()
                .serverId(serverId)
                .userId(userId).build();
    }
    public static ServerCRUDDTO create(
            UUID ownerId,
            String name) {
        return ServerCRUDDTO.builder()
                .userId(ownerId)
                .name(name).build();
    }

    public static ServerCRUDDTO delete(
            UUID serverId,
            UUID ownerId
    ) {
        return ServerCRUDDTO.builder()
                .serverId(serverId)
                .userId(ownerId).build();
    }

    public static ServerCRUDDTO update(
            UUID replaceServerId,
            UUID replaceOwnerId,
            String replaceName
    ) {
        return ServerCRUDDTO.builder()
                .serverId(replaceServerId)
                .userId(replaceOwnerId)
                .name(replaceName).build();
    }
}
