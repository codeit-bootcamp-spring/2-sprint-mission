package com.sprint.mission.discodeit.core.user.usecase.dto;

public record UserCreateCommand(
    String username,
    String email,
    String password
) {

}
