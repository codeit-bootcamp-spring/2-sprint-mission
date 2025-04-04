package com.sprint.mission.discodeit.dto.user;

public record UserCreateRequest(
    String username,
    String password,
    String nickname,
    String email
) {

}
