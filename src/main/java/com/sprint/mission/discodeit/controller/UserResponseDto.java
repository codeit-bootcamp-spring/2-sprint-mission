package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.dto.user.UserDto;

import java.util.UUID;

public record UserResponseDto(UUID id, String name, String email, UUID profileId, boolean isLogin) {
    public static UserResponseDto of(UserDto userDto, boolean isLogin) {
        return new UserResponseDto(userDto.id(), userDto.name(), userDto.email(), userDto.profileId(), isLogin);
    }
}
