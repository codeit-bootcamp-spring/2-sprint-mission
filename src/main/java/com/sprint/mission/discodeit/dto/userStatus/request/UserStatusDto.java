package com.sprint.mission.discodeit.dto.userStatus.request;

import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserStatusDto(
        @NotNull UUID id,
        @NotNull UUID userId,
        @NotNull Boolean onLineStatus
) {
    public static UserStatusDto from(@NotNull UserStatus userStatus) {
        return new UserStatusDto(userStatus.getId(), userStatus.getUserId(), userStatus.getOnlineStatus());
    }
}
