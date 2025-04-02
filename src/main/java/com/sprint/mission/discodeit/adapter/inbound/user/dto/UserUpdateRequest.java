package com.sprint.mission.discodeit.adapter.inbound.user.dto;

public record UserUpdateRequest(
    String newName,
    String newEmail
) {

}
