package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(
    UUID id,
    String username,
    String email,
    BinaryContentResponse profile,
    boolean online
) {

  public static UserResponse of(User user, BinaryContentResponse contentResponse,
      boolean online) {
    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(contentResponse)
        .online(online)
        .build();
  }
}
