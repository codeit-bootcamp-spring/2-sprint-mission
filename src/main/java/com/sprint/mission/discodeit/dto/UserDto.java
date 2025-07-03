package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.domain.Role;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    boolean online,
    Role role
) {

}
