package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentDto;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    boolean online
) {

  public static UserDto of(User user, BinaryContentDto contentResponse,
      boolean online) {
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(contentResponse)
        .online(online)
        .build();
  }
}
