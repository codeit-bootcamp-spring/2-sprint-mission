package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.constant.Role;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    Role role,
    BinaryContentDto profile,
    Boolean online
) {

}
