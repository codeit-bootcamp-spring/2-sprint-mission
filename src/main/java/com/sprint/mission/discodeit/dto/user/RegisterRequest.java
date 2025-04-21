package com.sprint.mission.discodeit.dto.user;

public record RegisterRequest(
    String username,
    String email,
    String password
) {

}
