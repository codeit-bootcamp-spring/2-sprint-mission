package com.sprint.mission.discodeit.DTO.User;

public record UserCreateDTO(
        String userName,
        String email,
        String password
) {
}

