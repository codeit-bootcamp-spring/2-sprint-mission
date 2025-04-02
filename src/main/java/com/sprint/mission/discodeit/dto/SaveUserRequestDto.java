package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record SaveUserRequestDto(
        String username,
        String password,
        String nickname,
        String email
) {
}
