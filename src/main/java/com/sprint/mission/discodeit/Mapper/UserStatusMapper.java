package com.sprint.mission.discodeit.Mapper;

import com.sprint.mission.discodeit.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {
    public UserStatusDto toDto(UserStatus userStatus) {
        return UserStatusDto.builder()
                .id(userStatus.getId())
                .userId(userStatus.getUser().getId())
                .lastActiveAt(userStatus.getLastActiveAt())
                .build();
    }
}