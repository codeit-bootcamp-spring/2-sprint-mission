package com.sprint.mission.discodeit.application.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.ZonedDateTime;
import java.util.List;

public record UserStatusesDto(List<UserStatusDto> useStatuses) {
    public static UserStatusesDto fromEntity(List<UserStatus> userStatuses) {
        List<UserStatusDto> userStatusDtos = userStatuses.stream()
                .map(userStatus -> UserStatusDto.fromEntity(userStatus, userStatus.isLogin(ZonedDateTime.now().toInstant())))
                .toList();

        return new UserStatusesDto(userStatusDtos);
    }
}
