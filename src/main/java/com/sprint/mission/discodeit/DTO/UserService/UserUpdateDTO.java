package com.sprint.mission.discodeit.DTO.UserService;

import java.util.UUID;

public record UserUpdateDTO(
        UUID id,
        String userName,
        String email,
        String password,
        byte[] photo
) {

}
