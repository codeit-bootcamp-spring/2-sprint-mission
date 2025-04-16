package com.sprint.mission.discodeit.core.user.usecase.dto;

import com.sprint.mission.discodeit.core.content.usecase.dto.BinaryContentResult;
import com.sprint.mission.discodeit.core.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "User Item")
public record UserResult(
    @Schema(description = "User Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,

    @Schema(description = "User 이름", example = "string")
    String username,

    @Schema(description = "User 이메일", example = "string")
    String email,

    @Schema(description = "User 프로필 Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    BinaryContentResult profile,

    @Schema(description = "User 온라인 상태", example = "true")
    boolean online
) {

  public static UserResult create(User user, boolean online) {
    if (user.getProfile() == null) {
      return UserResult.builder()
          .id(user.getId())
          .username(user.getName())
          .email(user.getEmail())
          .online(online).build();
    } else {
      return UserResult.builder()
          .id(user.getId())
          .username(user.getName())
          .email(user.getEmail())
          .profile(BinaryContentResult.create(user.getProfile()))
          .online(online).build();
    }
  }
}
