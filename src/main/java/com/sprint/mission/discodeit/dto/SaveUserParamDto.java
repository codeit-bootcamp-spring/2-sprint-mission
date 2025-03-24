package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record SaveUserParamDto(
        String username,
        String password,
        String nickname,
        String email,
        UUID profileUUID
) {
}
