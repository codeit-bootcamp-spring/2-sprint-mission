package com.sprint.mission.discodeit.mapper.Impl;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final BinaryContentMapper binaryContentMapper;

    @Override
    public UserDto toDto(User user) {
        BinaryContentDto profileDto = Optional.ofNullable(user.getProfile())
                .map(binaryContentMapper::toDto)
                .orElse(null);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profileDto,
                user.getStatus().isOnline()
        );
    }
}
