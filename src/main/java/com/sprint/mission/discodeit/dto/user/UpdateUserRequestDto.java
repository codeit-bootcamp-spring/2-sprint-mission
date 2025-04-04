package com.sprint.mission.discodeit.dto.user;

public record UpdateUserRequestDto(
    String username,
    String password,
    String email
) {

}
