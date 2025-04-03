package com.sprint.mission.discodeit.service.dto.authdto;

public record AuthServiceLoginRequest(
        String username,
        String password
) {
}