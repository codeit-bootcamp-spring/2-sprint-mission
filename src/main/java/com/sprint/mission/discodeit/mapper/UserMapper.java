package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper{
    private final BinaryContentMapper binaryContentMapper;

    public UserDto toDto(User user){
        if(user == null){
            return null;
        }

        BinaryContentDto profileDto = binaryContentMapper.toDto(user.getProfile());
        Instant now = Instant.now();
        Boolean online = user.getStatus() != null &&
                user.getStatus().getLastActiveAt() != null &&
                now.minus(Duration.ofMinutes(5)).isBefore(user.getStatus().getLastActiveAt());

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profileDto,
                online
        );
    }

    public List<UserDto> toDtoList(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }

        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
