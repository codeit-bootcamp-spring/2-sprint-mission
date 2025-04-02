package com.sprint.mission.discodeit.dto.request;

public record UserCreateRequest(
    String name,
    String email,
    String password
) {

}
