package com.sprint.mission.discodeit.core.user.usecase.dto;

public record LoginUserCommand(
    String userName,
    String password
) {

}
