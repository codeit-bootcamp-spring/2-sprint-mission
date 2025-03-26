package com.sprint.mission.discodeit.dto.user;

public record UpdateUserReqDto(
        String username,
        String email,
        String password,
        byte[] profileFile
) {
}
