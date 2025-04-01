package com.sprint.mission.discodeit.core.user.usecase.crud.dto;

public record CreateUserCommand(
    String name,
    String email,
    String password
) {

}
