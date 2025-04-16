package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.userdto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BinaryContentMapper binaryContentMapper;

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile() != null ? binaryContentMapper.toDto(user.getProfile()) : null,
                user.getStatus().currentUserStatus()
        );
    }
}
