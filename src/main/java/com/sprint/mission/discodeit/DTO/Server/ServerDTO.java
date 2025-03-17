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

}
