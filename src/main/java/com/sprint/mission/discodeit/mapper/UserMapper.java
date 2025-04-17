package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user) {
    boolean isOnline = Optional.ofNullable(user.getStatus())
        .map(UserStatus::isOnline)
        .orElse(false);

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        Optional.ofNullable(user.getProfile())
            .map(binaryContentMapper::toDto)
            .orElse(null),  // null-safe 처리
        isOnline
    );
  }
}