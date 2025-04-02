package com.sprint.mission.discodeit.core.user.usecase;

public record LoginUserCommand(
    String userName,
    String password
) {

}
