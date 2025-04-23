package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user) {
    boolean online = user.getStatus().isOnline();
    BinaryContentDto binaryContentDto = binaryContentMapper.toDto(user.getProfile());

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        binaryContentDto,
        online
    );
  }
}
