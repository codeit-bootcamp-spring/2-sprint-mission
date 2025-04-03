package com.sprint.mission.discodeit.dto;

public record UpdateUserRequestDto(
    String username,
    String password,
    String email
) {

}
