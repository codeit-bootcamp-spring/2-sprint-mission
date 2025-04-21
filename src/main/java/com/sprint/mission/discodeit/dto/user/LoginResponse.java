package com.sprint.mission.discodeit.dto.user;

public record LoginResponse(
    boolean success,
    String message,
    String token
) {

}
