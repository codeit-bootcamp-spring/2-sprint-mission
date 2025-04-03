package com.sprint.mission.discodeit.adapter.inbound.user.response;

import com.sprint.mission.discodeit.core.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "User가 성공적으로 생성됨")
public record UserCreateResponse(
//    boolean success,
//    String message
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    String profiledId
) {

  public static UserCreateResponse create(User user) {
    return UserCreateResponse.builder()
        .id(user.getId())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .username(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .profiledId(user.getPassword()).build();
  }
}
