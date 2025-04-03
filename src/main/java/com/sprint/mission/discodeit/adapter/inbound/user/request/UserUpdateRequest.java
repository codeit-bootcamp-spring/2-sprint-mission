package com.sprint.mission.discodeit.adapter.inbound.user.request;

public record UserUpdateRequest(
    String newName,
    String newEmail
) {

}
