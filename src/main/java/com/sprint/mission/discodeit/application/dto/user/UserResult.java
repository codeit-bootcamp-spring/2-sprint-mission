package com.sprint.mission.discodeit.application.dto.user;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public record UserResult(UUID id, String name, String email, UUID profileId) {
    public static UserResult fromEntity(User user) {
        return new UserResult(user.getId(), user.getName(), user.getEmail(), user.getProfileId());
    }

    public static List<UserResult> fromEntity(List<User> users) {
        return users.stream()
                .map(UserResult::fromEntity)
                .toList();
    }
}
