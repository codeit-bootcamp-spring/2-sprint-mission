package com.sprint.mission.discodeit.domain.user.dto;

import com.sprint.mission.discodeit.domain.storage.dto.BinaryContentDto;
import com.sprint.mission.discodeit.domain.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.user.entity.Role;
import com.sprint.mission.discodeit.domain.user.entity.User;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto(
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online,
    Role role
) {

  public static UserDto from(User user) {
    BinaryContent userProfile = user.getProfile();
    BinaryContentDto contentDto = null;
    if (userProfile != null) {
      contentDto = BinaryContentDto.from(userProfile);
    }
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(contentDto)
        .role(user.getRole()).build();
  }

  public static UserDto of(User user, boolean online) {
    BinaryContent userProfile = user.getProfile();
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(userProfile != null ? BinaryContentDto.from(userProfile) : null)
        .online(online)
        .role(user.getRole())
        .build();
  }
}
