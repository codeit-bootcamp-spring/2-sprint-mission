package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  public UserDto toDto(User user) {
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile() != null ? binaryContentMapper.toDto(user.getProfile(),
            binaryContentStorage.get(user.getProfile().getId())) : null,
        user.getStatus().isLastStatus()
    );
  }
}
