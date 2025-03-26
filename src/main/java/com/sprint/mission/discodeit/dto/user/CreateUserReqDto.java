package com.sprint.mission.discodeit.dto.user;

public record CreateUserReqDto(
        String username,
        String email,
        String password,
        byte[] profileFile
) {
}
