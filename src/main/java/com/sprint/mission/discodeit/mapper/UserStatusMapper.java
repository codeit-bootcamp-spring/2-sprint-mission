package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserStatusMapper {
    public UserStatusDto toDto(UserStatus userStatus) {
        if(userStatus == null) {
            return null;
        }

        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getUser().getId(),
                userStatus.getLastActiveAt()
        );
    }

    public List<UserStatusDto> toDto(List<UserStatus> userStatuses) {
        if(userStatuses == null) {
            return Collections.emptyList();
        }

        return userStatuses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
