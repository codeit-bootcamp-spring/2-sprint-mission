package com.sprint.mission.discodeit.dto.service.user;

import java.util.UUID;

public record UpdateUserParam(
    String newUsername,
    String newEmail,
    String newPassword
) {

}
