package com.sprint.mission.discodeit.dto.UserService;

import java.util.UUID;

public record UserUpdateRequest(
        UUID id,
        String userName,
        String email,
        String password,
        byte[] photo
) {

}
