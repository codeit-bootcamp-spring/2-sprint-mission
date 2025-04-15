package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online
) {

  public static UserDto from(User user) {
    if (user == null) {
      return null;
    }

    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        BinaryContentDto.from(user.getProfile()),
        user.getStatus().isUserOnline()
    );
  }
}
