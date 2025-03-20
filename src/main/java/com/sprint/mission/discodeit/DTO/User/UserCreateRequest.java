package com.sprint.mission.discodeit.DTO.User;

public record UserCreateRequest(
        String username,
        String email,
        String password
) {}
