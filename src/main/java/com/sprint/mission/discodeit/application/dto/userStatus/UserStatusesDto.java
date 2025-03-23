package com.sprint.mission.discodeit.application.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;

public record UserStatusesDto(List<UserStatusDto> useStatuses) {
    public static UserStatusesDto fromEntity(List<UserStatus> userStatuses) {
        List<UserStatusDto> userStatusDtos = userStatuses.stream()
                .map(UserStatusDto::fromEntity)
                .toList();

        return new UserStatusesDto(userStatusDtos);
    }
}
