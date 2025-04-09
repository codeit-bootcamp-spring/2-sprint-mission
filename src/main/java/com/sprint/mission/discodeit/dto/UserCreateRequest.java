package com.sprint.mission.discodeit.dto;

public record UserCreateRequest(
    String email,
    String password,
    String username
) {

}
