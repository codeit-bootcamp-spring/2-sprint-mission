package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.dto.userstatusdto.UserStatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserStatusMapper {

    public UserStatusResponseDto toDto(UserStatus userStatus) {
        return new UserStatusResponseDto(
                userStatus.getId(),
                userStatus.getUser().getId(),
                userStatus.getLastActiveAt()
        );
    }

    public UserStatusResponseDto toDto1(UserStatus userStatus) {
        return UserStatusResponseDto.builder()
                .id(userStatus.getId())
                .userId(userStatus.getUser().getId())
                .lastActiveAt(userStatus.getLastActiveAt())
                .build();

    }
}
