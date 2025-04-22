package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user){
        BinaryContentDto profileDto = Optional.ofNullable(user.getProfile())
                .map(binaryContentMapper::toDto)
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profileDto,
                Optional.ofNullable(user.getUserStatus()).map(UserStatus::isOnline).orElse(null)
        );
    }
}
