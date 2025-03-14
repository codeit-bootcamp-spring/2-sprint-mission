package com.sprint.mission.discodeit.DTO.User;

import com.sprint.mission.discodeit.entity.BinaryContent;

public record UserCreateDTO(
        String userName,
        String email,
        String password,
        BinaryContent binaryContent
) {
}

