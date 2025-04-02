package com.sprint.mission.discodeit.adapter.inbound.user.dto;

import java.util.UUID;

public record UpdateUserCommand(
    UUID requestUserId,
    String replaceName,
    String replaceEmail
) {

}
