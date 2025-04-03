package com.sprint.mission.discodeit.adapter.inbound.user.response;

import com.sprint.mission.discodeit.core.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "로그인 성공 응답")
public record UserLoginResponse(
//    boolean success,
//    String token
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    String profiledId
) {

  public static UserLoginResponse create(User user) {
    return UserLoginResponse.builder()
        .id(user.getId())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .username(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .profiledId(user.getPassword()).build();
  }

}
