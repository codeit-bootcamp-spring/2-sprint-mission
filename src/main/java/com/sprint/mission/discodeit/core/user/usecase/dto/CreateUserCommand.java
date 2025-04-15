package com.sprint.mission.discodeit.core.user.usecase.dto;

public record CreateUserCommand(
    String username,
    String email,
    String password
) {

}
