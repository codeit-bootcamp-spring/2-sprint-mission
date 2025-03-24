package com.sprint.mission.discodeit.service.dto.userdto;

import java.nio.file.Path;
import java.util.UUID;

public record UserUpdateDto(
        UUID userId,
        String changeName,
        String changeEmail,
        String changePassword,
        Path path
) {

}
