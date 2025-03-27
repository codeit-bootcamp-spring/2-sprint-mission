package com.sprint.mission.discodeit.application.dto.user;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public record UsersDto(List<UserDto> users) {
    public static UsersDto fromEntity(List<User> users) {
        List<UserDto> userDtos = users.stream()
                .map(UserDto::fromEntity)
                .toList();

        return new UsersDto(userDtos);
    }
}
