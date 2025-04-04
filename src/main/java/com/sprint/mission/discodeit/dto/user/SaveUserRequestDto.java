package com.sprint.mission.discodeit.dto.user;

public record SaveUserRequestDto(
    String username,
    String password,
    String nickname,
    String email
) {

}
