package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.file.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        BinaryContentDto profileDto = binaryContentMapper.toDto(user.getProfile());

        Boolean online = null;
        UserStatus status = user.getStatus();
        if (status != null) {
            online = status.online();
        }

        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            profileDto,
            online
        );
    }
}
