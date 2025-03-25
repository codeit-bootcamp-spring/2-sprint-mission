package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record LoginDto(
        String userName,
        String userPassword
) {
}
