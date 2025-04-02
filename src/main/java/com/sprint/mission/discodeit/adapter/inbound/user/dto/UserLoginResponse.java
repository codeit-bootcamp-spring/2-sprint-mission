package com.sprint.mission.discodeit.adapter.inbound.user.dto;

public record UserLoginResponse(
    boolean success,
    String token
) {

}
