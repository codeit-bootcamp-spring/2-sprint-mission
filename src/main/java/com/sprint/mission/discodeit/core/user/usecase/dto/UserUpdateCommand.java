package com.sprint.mission.discodeit.core.user.usecase.dto;

import java.util.UUID;

public record UserUpdateCommand(
    UUID requestUserId,
    String newName,
    String newEmail,
    String newPassword
) {

}
