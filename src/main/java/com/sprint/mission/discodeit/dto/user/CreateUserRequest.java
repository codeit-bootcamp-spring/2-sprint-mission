package com.sprint.mission.discodeit.dto.user;

public record CreateUserRequest(
    String email,
    String username,
    String password
) {

}