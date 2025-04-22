package com.sprint.mission.discodeit.dto.service.user;

public record CreateUserCommand(
    String username,
    String email,
    String password
) {

}
