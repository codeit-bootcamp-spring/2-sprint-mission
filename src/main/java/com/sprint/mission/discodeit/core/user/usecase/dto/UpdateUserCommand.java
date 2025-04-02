package com.sprint.mission.discodeit.core.user.usecase.dto;

import java.util.UUID;

public record UpdateUserCommand(
    UUID requestUserId,
    String newName,
    String newEmail
) {

}
