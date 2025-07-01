package com.sprint.mission.discodeit.core.user.dto;

import com.sprint.mission.discodeit.core.storage.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.storage.entity.BinaryContent;
import com.sprint.mission.discodeit.core.user.entity.Role;
import com.sprint.mission.discodeit.core.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "User Item")
public record UserDto(
    @Schema(description = "User Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,

    @Schema(description = "User 이름", example = "string")
    String username,

    @Schema(description = "User 이메일", example = "string")
    String email,

    @Schema(description = "User 프로필")
    BinaryContentDto profile,

    @Schema(description = "User 권한")
    Role role,

    @Schema(description = "User 온라인 상태", example = "true")
    boolean online
) {

  public static UserDto from(User user) {
    BinaryContent userProfile = user.getProfile();
    BinaryContentDto binaryContentDto = null;
    if (userProfile != null) {
      binaryContentDto = BinaryContentDto.create(userProfile);
    }
    return UserDto.builder()
        .id(user.getId())
        .username(user.getName())
        .email(user.getEmail())
        .profile(binaryContentDto)
        .role(user.getRole())
        .online(user.isOnline()).build();
  }
}
