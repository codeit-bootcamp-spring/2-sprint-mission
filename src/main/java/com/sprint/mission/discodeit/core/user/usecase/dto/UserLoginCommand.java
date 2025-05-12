package com.sprint.mission.discodeit.core.user.usecase.dto;

public record UserLoginCommand(
    String username,
    String password
) {

}
