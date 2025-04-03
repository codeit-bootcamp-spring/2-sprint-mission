package com.sprint.mission.discodeit.adapter.inbound.user.response;

import com.sprint.mission.discodeit.core.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "User 정보가 성공적으로 수정됨")
public record UserUpdateResponse(
//    boolean success
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    String profiledId
) {

  public static UserUpdateResponse create(User user) {
    return UserUpdateResponse.builder()
        .id(user.getId())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .username(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .profiledId(user.getPassword()).build();
  }

}
