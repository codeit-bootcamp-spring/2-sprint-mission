package com.sprint.mission.discodeit.core.user.usecase.dto;

public record LoginUserCommand(
    String username,
    String password
) {

}
