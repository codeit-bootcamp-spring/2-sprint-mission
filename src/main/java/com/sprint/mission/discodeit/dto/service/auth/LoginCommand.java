package com.sprint.mission.discodeit.dto.service.auth;

public record LoginCommand(
    String username,
    String password
) {

}
