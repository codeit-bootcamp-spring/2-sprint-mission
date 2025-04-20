package com.sprint.mission.discodeit.dto.service.user;

public record UserCreateRequest(
    String username,
    String email,
    String password
) {

}