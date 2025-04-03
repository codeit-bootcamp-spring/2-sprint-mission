package com.sprint.mission.discodeit.dto;

public record SaveUserRequestDto(
    String username,
    String password,
    String nickname,
    String email
) {

}
