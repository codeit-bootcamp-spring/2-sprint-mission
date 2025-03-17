package com.sprint.mission.discodeit.DTO.Server;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ServerCRUDDTO(
        UUID serverId,
        UUID userId,
        String name
) {



}
