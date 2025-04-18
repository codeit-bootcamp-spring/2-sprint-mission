package com.sprint.mission.discodeit.mapper.Impl;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapperImpl implements UserStatusMapper {
    @Override
    public UserStatusDto toDto(UserStatus userStatus) {
        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUser().getId(),
                userStatus.getLastActiveAt()

        );
    }
}