package com.sprint.mission.discodeit.service.dto.authdto;

public record AuthServiceLoginRequest(
        String name,
        String password
) {
}
